/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package clinisoft.services;

import java.util.Map;

import clinisoft.interfaces.AuthenticationService;
import clinisoft.interfaces.implement.AuthenticationServiceImpl;
import clinisoft.services.UserCredential;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AuthenticationInit implements Initiator {

	//services
	AuthenticationService authService = new AuthenticationServiceImpl();
	
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		
		UserCredential cre = authService.getUserCredential();
		if(cre==null || cre.isAnonymous()){
			Executions.sendRedirect("/login.zul");
			return;
		}
	}
}