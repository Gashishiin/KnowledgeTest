package controllers;

import DAO.UsersDAO;
import base.UserRole;
import base.Users;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {
    private final String ALREADY_EXISTS = "Пользователь %s уже существует";
    private final String EMPTY_FIELDS = "Не все поля заполнены";
    private final String DIFFERENT_PASSWORDS = "Введенный пароль и пароль для подтверждения не совпадают";
    private final String PASSWORD_MINIMAL_LENGTH = "Минимальная длина пароля 6 символов";
    private final String WRONG_CHARACTERS_USER_LOGIN =
            "Минимальная длина логина 3 символа, допустимы буквы латиницы, цифры, точка, тире и подчеркивание";
    private final String EMPTY_FULLNAME_FIELD = "Не заполнено поле \"Полное имя\"";
    private final String CAN_NOT_DELETE_YOURSELF = "Пользователь не может удалить себя";
    private final String CAN_NOT_UPDATE_OWN_ROLE = "Пользователь не может изменить собственную роль";
    private final String CAN_NOT_CHANGE_USER_ADMIN = "Невозможно изменить роль или удалить пользователя \"admin\"";
    @RequestMapping("users")
    public String getUsers(WebRequest request, Model model) {
        List<Users> usersList = new UsersDAO().retrieveAllUsers();
        Collections.sort(usersList, new Comparator<Users>() {
            public int compare(Users o1, Users o2) {
                return o1.getFullname().compareTo(o2.getFullname());
            }
        });
        model.addAttribute("userlist", usersList);
        return "users";
    }

    @RequestMapping(value = "createuser")
    public String createUser(WebRequest request, RedirectAttributes attributes) {
        Map<String,String> params = new HashMap<String, String>();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String fullname = request.getParameter("fullname");
        String userrole = request.getParameter("role").toUpperCase();
        params.put("login",login);
        params.put("password",password);
        params.put("password2",password2);
        params.put("fullname",fullname);
        List<String> errorMessageList = validateNewUserForm(params);
        if (errorMessageList.size() == 0)
            new UsersDAO().createUser(login, password, fullname, UserRole.valueOf(userrole));
        else
            attributes.addFlashAttribute("errormessagelist",errorMessageList);
        return "redirect:users";
    }

    @RequestMapping(value = "getuser")
    @ResponseBody
    public String getUser(HttpServletRequest request, Model model){
        CsrfToken token = new HttpSessionCsrfTokenRepository().loadToken(request);
        long userID = Long.parseLong(request.getParameter("userid"));
        Users user = new UsersDAO().retrieveUserByID(userID);
        String htmlBody = "<form action='updateuser' method='POST'>\n" +
                "        Логин<br/>\n" +
                "        <input readonly type='text' name='login' value='" + user.getLogin() + "'><br/>\n" +
                "        Полное имя<br/>\n" +
                "        <input type='text' name='fullname' value='" + user.getFullname()+"'><br/>\n" +
                "        Пароль <br/>\n" +
                "        <input type='password' name='password'><br/>\n" +
                "        Подтверждение пароля<br/>\n" +
                "        <input type='password' name='password2'><br/>\n" +
                "        Роль<br/>\n" +
                "        <select name='role'>\n" +
                "            <option value='role_admin' " + (user.getUserRole()==UserRole.ROLE_ADMIN ? "selected":"") + ">Администратор</option>\n" +
                "            <option value='role_methodist' "  + (user.getUserRole()==UserRole.ROLE_METHODIST ? "selected":"") + ">Методист</option>\n" +
                "            <option value='role_student' " + (user.getUserRole()==UserRole.ROLE_STUDENT ? "selected":"") +">Студент</option>\n" +
                "        </select><br/>\n" +
                "        <p style='align-content: center'><input type='submit' value='Обновить пользователя'>" +
                "<button style='margin-left: 5px;' type='button' onclick='location.href=\"users\"'>Отмена</button></p>\n" +
                "        <input type='hidden' name='userid' value='" +user.getUserID() + "'>\n"+
                "        <input type='hidden' name='_csrf' value='"+ token.getToken() + "'>\n" +
                "    </form>";
        return htmlBody;
    }


    @RequestMapping(value = "deleteusers")
    public String deleteUsers(WebRequest request, RedirectAttributes attributes) {
        String userLogins[] = request.getParameterValues("login");
        String currentLoggedUser = request.getUserPrincipal().getName();
        List<String> errorMessageList = new ArrayList<String>();
        if (userLogins != null) {
            if (Arrays.asList(userLogins).contains("admin"))
                errorMessageList.add(CAN_NOT_CHANGE_USER_ADMIN);
            if (Arrays.asList(userLogins).contains(currentLoggedUser))
                errorMessageList.add(CAN_NOT_DELETE_YOURSELF);
                attributes.addFlashAttribute("errormessagelist",errorMessageList);
            if (errorMessageList.size() == 0)
             new UsersDAO().deleteUsers(userLogins);
        }
        return "redirect:users";
    }

    @RequestMapping(value = "updateuser")
    public String updateUser(WebRequest request, RedirectAttributes attributes){
        Map<String,String> params = new HashMap<String, String>();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String fullname = request.getParameter("fullname");
        String userrole = request.getParameter("role").toUpperCase();
        long userID = Long.parseLong(request.getParameter("userid"));
        Users currentUser = new UsersDAO().retrieveUser(request.getUserPrincipal().getName());
        String currentRole = currentUser.getUserRole().toString();
        params.put("password",password);
        params.put("password2",password2);
        params.put("fullname",fullname);
        params.put("userrole",userrole);
        List<String> errorMessageList = validateExistUserForm(params);
        if (!currentRole.equalsIgnoreCase(userrole) && currentUser.getUserID() == userID)
            errorMessageList.add(CAN_NOT_UPDATE_OWN_ROLE);
        if (login.equals("admin") && UserRole.valueOf(userrole)!=UserRole.ROLE_ADMIN)
            errorMessageList.add(CAN_NOT_CHANGE_USER_ADMIN);
        if (errorMessageList.size() == 0){
            new UsersDAO().updateUser(userID,params);
        }else
            attributes.addFlashAttribute("errormessagelist",errorMessageList);
        return "redirect:users";
    }

    private List<String> validateExistUserForm(Map<String, String> params) {
        List<String> errorMessageList = new ArrayList<String>();
        String password = params.get("password");
        String password2 = params.get("password2");
        if (params.get("fullname").length()==0)
            errorMessageList.add(EMPTY_FULLNAME_FIELD);
        if (password.length() != 0 && password.length()!= 0 && !password.equals(password2))
            errorMessageList.add(DIFFERENT_PASSWORDS);
        return errorMessageList;
    }


    private List<String> validateNewUserForm(Map<String,String> params){
        List<String> errorMessageList = new ArrayList<String>();
        UsersDAO usersDAO = new UsersDAO();
        String login = params.get("login");
        String password = params.get("password");
        String password2 = params.get("password2");
        String fullname = params.get("fullname");
        Users user = usersDAO.retrieveUser(login);
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]{3,}$");
        Matcher matcher = pattern.matcher(login);
        if (login.length() == 0 || password.length() == 0 || password2.length() == 0 || fullname.length() == 0)
            errorMessageList.add(EMPTY_FIELDS);
        if (!password.equals(password2))
            errorMessageList.add(DIFFERENT_PASSWORDS);
        if (password.length() < 6 || password2.length() < 6)
            errorMessageList.add(PASSWORD_MINIMAL_LENGTH);
        if (user != null)
            errorMessageList.add(String.format(ALREADY_EXISTS,user.getLogin()));
        if (!matcher.matches())
            errorMessageList.add(WRONG_CHARACTERS_USER_LOGIN);
        return errorMessageList;
    }

    @PostConstruct
    public Users checkAdminExist(){
        UsersDAO usersDAO = new UsersDAO();
        Users user = usersDAO.retrieveUser("admin");
        if (user == null)
            usersDAO.createUser("admin","123456","Администратор", UserRole.ROLE_ADMIN);
        return user;
    }
}
