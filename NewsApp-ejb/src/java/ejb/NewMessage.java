package ejb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Finalgalaxy
 */
@JMSDestinationDefinition(
        name = "java:app/jms/NewMessage",
        interfaceName = "javax.jms.Queue",
        resourceAdapter = "jmsra",
        destinationName = "jmsNewMessage"
)

/* MessageDriven directive estabilishes that this class is a Message Driven Bean and specifies the JMS resource used by the bean,
   
*/
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/NewMessage"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})

@SuppressWarnings("CallToPrintStackTrace")
public class NewMessage implements MessageListener {
    @Resource private MessageDrivenContext mdc;
    @PersistenceContext(unitName = "NewsApp-ejbPU") private EntityManager em;
    
    public NewMessage() {}
    
    public void save(Object object) {  
        System.out.println("Invoked save");
        em.persist(object);
    }
    
    @Override
    public void onMessage(Message message) {
        System.out.println("Invoked onMessage");
        try{
            if(message instanceof ObjectMessage){
                save((NewsEntity)(((ObjectMessage)message).getObject()));
            }
        }catch(JMSException e){
            e.printStackTrace();
            mdc.setRollbackOnly();
        }catch(Throwable te){
            te.printStackTrace();
        }
    }
}
