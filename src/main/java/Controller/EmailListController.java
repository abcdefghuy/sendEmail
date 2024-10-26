package Controller;

import Data.UserDB;
import Model.User;
import Utill.MailUtilGmail;
import Utill.MailUtilLocal;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/EmailSendingServlet")
public class EmailListController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "join"; // default action
        }

        // perform action and set URL to appropriate page
        String url = "/index.jsp"; // default URL
        if (action.equals("join")) {
            url = "/index.jsp"; // the "join" page
        } else if (action.equals("add")) {
            // get parameters from the request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // store data in User object
            User user = new User();
            user.setEmailAddress(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            String message;
            if (UserDB.emailExists(user.getEmailAddress())) {
                message = "This email address already exists.<br>" +
                        "Please enter another email address.";
                url = "/index.jsp";
            }
            else {
                // send email to user
                message = "";
                UserDB.insert(user);
                String to = email;
                String from = "email_list@murach.com";
                String subject = "Welcome to our email list";
                String body = "Dear " + firstName + ",\n\n"
                        + "Thanks for joining our email list. We'll make sure to send "
                        + "you announcements about new products and promotions.\n"
                        + "Have a great day and thanks again!\n\n"
                        + "Kelly Slivkoff\n"
                        + "Mike Murach & Associates";
                boolean isBodyHTML = false;

                try {
                    MailUtilGmail.sendMail(to, from, subject, body, isBodyHTML);
                } catch (MessagingException e) {
                    String errorMessage = "ERROR: Unable to send email. "
                            + "Check Tomcat logs for details.<br>"
                            + "NOTE: You may need to configure your system "
                            + "as described in chapter 14.<br>"
                            + "ERROR MESSAGE: " + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);

                    this.log("Unable to send email. \n"
                            + "Here is the email you tried to send: \n"
                            + "TO: " + email + "\n"
                            + "FROM: " + from + "\n"
                            + "SUBJECT: " + subject + "\n"
                            + "\n" + body + "\n\n");
                }

                url = "/thanks.jsp"; // Redirect to the thank you page
            }
            request.setAttribute("user", user); // Store the user object in request
            request.setAttribute("message", message);


        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

}