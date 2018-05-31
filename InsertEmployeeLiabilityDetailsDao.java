package sf.claims.wc.transaction.manager.data.utility.data.dao;

import static sf.claims.wc.transaction.manager.data.utility.data.sql.InsertEmployeeLiabilityDetailsSql.INSERT_EMPLOYEE_LIABILITY_DETAILS_SQL;
import static sf.claims.wc.transaction.manager.data.utility.util.DataUtility.getPdqTemplate;
import static sf.claims.wc.transaction.manager.data.utility.util.DataUtilityConstants.RESULT_SUCCESS;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.UserTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.jta.WebSphereUowTransactionManager;
import org.springmodules.purequery.PdqTemplate;

import sf.claims.wc.common.utilities.model.data.utility.EmployeeLiability;
import sf.claims.wc.common.utilities.model.data.utility.EmployeeLiabilityRequest;
import sf.claims.wc.transaction.manager.data.utility.exception.WCTransactionManagerDataUtilityException;
import sf.claims.wc.transaction.manager.data.utility.util.UserTransactionManagement;

import com.ibm.pdq.runtime.HeterogeneousBatchKind;

/**
 * Dao Class for InsertEmployeeLiabilityDetails operation.
 * 
 * @author Mrunmayi Chamankar WJGY
 * @since 03/29/2018
 * @version 1.0.0
 */
@Component("insertEmployeeLiabilityDetailsDao")
public class InsertEmployeeLiabilityDetailsDao {

	 @Autowired
	 private UserTransactionManagement userTransactionManagement;
		
	 @Autowired
	 private WebSphereUowTransactionManager transactionManager;
		
  	/**
  	 * This method insert each employeeLiability data in WC_EMPLE_LIAB_XREF table.
  	 *  
  	 * @param employeeLiabilityRequest
  	 * @return String - Success or Failure status of insert operation.
  	 */
	public String insertEmployeeLiabilityDetails(EmployeeLiabilityRequest employeeLiabilityRequest){
		PdqTemplate pdqTemplate = getPdqTemplate(PdqTemplate.class);
  		UserTransaction userTransaction = transactionManager.getUserTransaction();
  		try {
	  			userTransactionManagement.beginUserTransaction(userTransaction);
		  		pdqTemplate.startBatch(HeterogeneousBatchKind.heterogeneousModify__);
		  	    for(EmployeeLiability employeeLiability : employeeLiabilityRequest.getEmployeeLiabilities())
		  	    {
		  	    	List<String> arguments = new ArrayList<>();
		  	    	pdqTemplate.update(buildDynamicQuery(employeeLiability, arguments),arguments.toArray());
		  	    }	
		  	    pdqTemplate.endBatch();
				userTransactionManagement.commitUserTransaction(userTransaction);
		  	    return RESULT_SUCCESS;
  		}
		catch (WCTransactionManagerDataUtilityException wcTransactionManagerDataUtilityException) {
			userTransactionManagement.rollBackUserTransaction(userTransaction);
	    	throw new WCTransactionManagerDataUtilityException(wcTransactionManagerDataUtilityException.getMessage());
		}
		
	}
	
	/**
  	 * This method build dynamic sql and arguments.
  	 * @param employeeLiability
  	 * @param arguments
  	 * @return String - dynamic sql
  	 */
  	 private String buildDynamicQuery(EmployeeLiability employeeLiability, List<String> arguments) {
		  StringBuilder dynamicSQL = new StringBuilder();
	  	  arguments.add(employeeLiability.getVendorClaimId());
	  	  arguments.add(employeeLiability.getVendorClaimTypeCode());
	  	  arguments.add(employeeLiability.getVendorEventNum());
	  	  arguments.add(employeeLiability.getSfClaimNum());
	  	  arguments.add(employeeLiability.getClaimantBirthDate());
	  	  arguments.add(employeeLiability.getClaimantSSN());
	  	  dynamicSQL.append(INSERT_EMPLOYEE_LIABILITY_DETAILS_SQL);
	  	  return dynamicSQL.toString();
  	}
}
