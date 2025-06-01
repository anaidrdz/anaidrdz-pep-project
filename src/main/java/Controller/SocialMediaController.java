package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::userRegistration);
        app.post("/login", this::userLogin);
        app.post("/messages", this::createNewMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccount);

        return app;
    }

    private void userRegistration(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addNewAccount = accountService.addNewAccount(account);
        if(addNewAccount != null) {
            ctx.json(mapper.writeValueAsString(addNewAccount));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    private void userLogin(Context ctx) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account userLogin = accountService.userLogin(account.getUsername(), account.getPassword());
        if(userLogin != null) {
            ctx.json(mapper.writeValueAsString(userLogin));
            ctx.status(200);
        }
        else {
            ctx.status(401);
        }
    }

    private void createNewMessage(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message successful = messageService.addNewMessage(message);

        if (successful != null) {
            ctx.json(mapper.writeValueAsString(successful));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) throws SQLException {
        ctx.json(messageService.getAllMessages());
        ctx.status(200);
    }

    private void getMessageById(Context ctx) throws SQLException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if(message != null) {
            ctx.json(message);
            ctx.status(200);
        }
        else {
            ctx.json("");
        }
    }   

    private void deleteMessageById(Context ctx) throws SQLException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMsg = messageService.deleteMessageById(messageId);
        if(deletedMsg != null) {
            ctx.json(deletedMsg);
            ctx.status(200);
        }
    }

    private void updateMessageById(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message updatedSuccessfully = messageService.updateMessage(messageId, message.getMessage_text());
        if(updatedSuccessfully != null) {
            ctx.json(mapper.writeValueAsString(updatedSuccessfully));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    private void getAllMessagesByAccount(Context ctx) throws SQLException {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByAccount(accountId));
        ctx.status(200);
    }
}