package com.bjsasc.twc.brace.util;


import com.bjsasc.platform.context.PtContext;


import com.bjsasc.platform.context.ThreadConfigBean;
import com.cascc.platform.aa.AAProvider;
import com.cascc.platform.aa.api.data.AAUserData;
import com.cascc.platform.aa.api.util.AAUtil;

public class ContextHelper {

	/**
	 * ��ȡ��ǰ�û���InnerId
	 * 
	 * @return ��ǰ�û���InnerId
	 */
	public static String getCurrentUserIId() {
		// ��ȡ��ǰ�߳�context
		PtContext ptContext = ThreadConfigBean.getPtPerson();
		return ptContext.getUserIID();
	}

	/**
	 * ��ȡ��ǰ�û�
	 * 
	 * @return ��ǰ�û�
	 */
	public static AAUserData getCurrentUser() {
		String userIId = getCurrentUserIId();
		return AAProvider.getUserService().getUser(AAUtil.getAAContext(),
				userIId);
	}
	
}
