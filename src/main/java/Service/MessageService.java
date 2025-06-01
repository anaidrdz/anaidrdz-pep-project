package Service;

import DAO.MessageDAO;
import Model.Message;
import DAO.AccountDAO;
import Model.Account;

import java.sql.SQLException;
import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public Message addNewMessage(Message message) throws SQLException {
       
        if(message.getMessage_text() == null || 
            message.getMessage_text().isBlank() || 
            message.getMessage_text().length() > 255) 
            {
            
            return null;
        }

        Account account = accountDAO.getAccountWithId(message.getPosted_by());
        if(account == null) {
            return null;
        }

        return messageDAO.addNewMessage(message);

    }

    public List<Message> getAllMessages() throws SQLException{
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) throws SQLException{
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) throws SQLException{
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(int messageId, String newMessage) throws SQLException{
        if(newMessage == null ||
            newMessage.isBlank() ||
            newMessage.length() > 255) {
                return null;
            }
        return messageDAO.updateMessage(newMessage, messageId);
    }

    public List<Message> getAllMessagesByAccount(int accountId) throws SQLException{
        return messageDAO.getAllMessagesByAccount(accountId);
    }
}
