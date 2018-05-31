package sf.claims.wc.transaction.manager.data.utility.util;

import static sf.claims.wc.transaction.manager.data.utility.util.DataUtilityConstants.BEGIN_TRANSACTION_EXCEPTION;
import static sf.claims.wc.transaction.manager.data.utility.util.DataUtilityConstants.TRANSACTION_ROLLED_BACK;
import static sf.claims.wc.transaction.manager.data.utility.util.DataUtilityConstants.ROLLBACK_TRANSACTION_EXCEPTION;
import static sf.claims.wc.transaction.manager.data.utility.util.DataUtilityConstants.COMMIT_ILLEGAL_EXCEPTION;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.springframework.stereotype.Component;

import sf.claims.wc.transaction.manager.data.utility.exception.WCTransactionManagerDataUtilityException;
import sf.jra.framework.eventing.Log;
import sf.jra.framework.eventing.LogFactory;

/** This class consist of methods for managing the user transactions.
 * 
 * @author Akshay Satam(RRAP)
 * @version 1.0
 * @date 01/16/2018
 * 
 * */
@Component("userTransactionManagement")
public class UserTransactionManagement {
	
	private static final Log LOGGER = LogFactory
			.getLog(UserTransactionManagement.class);
    
    /**
     * This method start the user transaction .
     * 
     * @param UserTransaction
     * @return void
     */
    
    public void beginUserTransaction(UserTransaction userTransaction){
            try {
				userTransaction.begin();
			} catch (NotSupportedException | SystemException e) {
				throw new WCTransactionManagerDataUtilityException(BEGIN_TRANSACTION_EXCEPTION);
			}
    }
    
    /**
     * This method rollback the user transaction if any exception occurs.
     * 
     * @param UserTransaction
     * @return void
     */
    
    public void rollBackUserTransaction(UserTransaction userTransaction) {
            try {
				userTransaction.rollback();
				LOGGER.info(TRANSACTION_ROLLED_BACK);
			} catch (IllegalStateException | SecurityException
					| SystemException e) {
				throw new WCTransactionManagerDataUtilityException(ROLLBACK_TRANSACTION_EXCEPTION);
			}
    }
    
    /**
     * This method commit the user transaction .
     * 
     * @param UserTransaction
     * @return void
     */
    
    public void commitUserTransaction(UserTransaction userTransaction){
            try {
				userTransaction.commit();
			} catch (SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				rollBackUserTransaction(userTransaction);
				throw new WCTransactionManagerDataUtilityException(COMMIT_ILLEGAL_EXCEPTION);
			}
        
    }
}

