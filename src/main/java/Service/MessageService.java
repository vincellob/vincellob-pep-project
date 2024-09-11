package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;
import java.util.Collections;

public class MessageService {
    private MessageDAO messageDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
    }
   
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        List<Message> message = messageDAO.getAllMessages();
    
        return message;

    }
    
    public Message addMessage(Message message) {
        Message persistedMessage = messageDAO.insertMessage(message);


        return persistedMessage;
    }

    public Message updateMessage(int message_id, Message message) {
        Message existingMessage = messageDAO.getMessageById(message_id);
    
        if (existingMessage == null) {
            return null; 
        }
    
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }
    
        messageDAO.updateMessage(message_id, message);
    
        Message updatedMessage = messageDAO.getMessageById(message_id);
        return updatedMessage;
    }

    public Message deleteMessage(int message_id) {
        Message messageToDelete = messageDAO.getMessageById(message_id);

        if (messageToDelete == null) {
            return null;
        }

        messageDAO.deleteMessage(message_id);
        return messageToDelete;
    }
    

    public List<Message> getAllMessagesFromAccountId(int id) {
        List<Message> messages = messageDAO.getAllMessagesFromAccount(id);
        
        if (messages == null || messages.isEmpty()) {
            return Collections.emptyList(); 
        }
    
        return messages;
    }


    public Message getMessageFromId(int id) {
        Message message = messageDAO.getMessageById(id);
    
        return message;

    }
}


