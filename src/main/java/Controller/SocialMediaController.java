package Controller;

import java.util.Collections;
import Model.Account;
import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SocialMediaController {
    AccountDAO accountDAO;
    MessageDAO messageDAO;
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountDAO = new AccountDAO();
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postAccountLoginHandler);
        app.post("/messages", this::postAddMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
        //app.start(8080);
        return app;
    }

    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    
    private void postAccountLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        try {
            Account account = mapper.readValue(ctx.body(), Account.class);

            boolean verify = accountService.validateLogin(account.getUsername(), account.getPassword());
            

            if (verify) {
                Account verifiedAccount = accountDAO.getAccountByUsername(account);
                ctx.json(verifiedAccount);
                ctx.status(200);
            } else {
                ctx.status(401);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void postAddMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);

        if (messages == null) {
            ctx.status(200);
        } else {
            ctx.json(messages);;
        }
    }

    private void getMessageByIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageById = messageService.getMessageFromId(message_id);

        if (messageById == null) {
            ctx.status(200);
        } else {
            ctx.json(messageById);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(message_id);
        if(deletedMessage!=null){
            ctx.json(deletedMessage);
        }else{
            ctx.status(200);
        }
    }

    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        if(updatedMessage!=null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    private void getMessagesByAccountIdHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesFromAccountId(account_id);
    
        if (messages == null || messages.isEmpty()) {
            ctx.json(Collections.emptyList());
            ctx.status(200);
        } else {
            ctx.json(messages);
            ctx.status(200);
        }
    }
}