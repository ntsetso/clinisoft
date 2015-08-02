package clinisoft.interfaces.implement;

import java.io.Serializable;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import clinisoft.entity.User;
import clinisoft.interfaces.AuthenticationService;
import clinisoft.interfaces.UserInfoService;
import clinisoft.services.UserCredential;

public class AuthenticationServiceImpl implements AuthenticationService,Serializable{
private static final long serialVersionUID = 1L;

UserInfoService userInfoService = new UserInfoServiceImpl();
	
public UserCredential getUserCredential(){
	Session sess = Sessions.getCurrent();
	UserCredential cre = (UserCredential)sess.getAttribute("userCredential");
	if(cre==null){
		cre = new UserCredential();//new a anonymous user and set to session
		sess.setAttribute("userCredential",cre);
	}
	return cre;
}

	public boolean login(String nm, String pd) {
		User user = userInfoService.findUser(nm);
		//a simple plain text password verification
		if(user==null || !user.getPassword().equals(pd)){
			return false;
		}
		
		Session sess = Sessions.getCurrent();
		UserCredential cre = new UserCredential(user.getAccount(),user.getFullName());
		//just in case for this demo.
		if(cre.isAnonymous()){
			return false;
		}
		sess.setAttribute("userCredential",cre);
		
		//TODO handle the role here for authorization
		return true;
	}
	
	public void logout() {
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("userCredential");
	}
}
