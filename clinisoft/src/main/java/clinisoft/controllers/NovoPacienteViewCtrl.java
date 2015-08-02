/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package clinisoft.controllers;

import java.util.Set;

import clinisoft.interfaces.implement.AuthenticationServiceImpl;
import clinisoft.interfaces.implement.UserInfoServiceImpl;
import clinisoft.entity.User;
import clinisoft.interfaces.AuthenticationService;
import clinisoft.services.CommonInfoService;
import clinisoft.services.UserCredential;
import clinisoft.interfaces.UserInfoService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

public class NovoPacienteViewCtrl extends SelectorComposer<Component>{
	private static final long serialVersionUID = 1L;

	//wire components
	@Wire
	Label account;
	@Wire
	Textbox fullName;
	@Wire
	Textbox email;
	@Wire
	Datebox birthday;
	@Wire
	Listbox country;
	@Wire
	Textbox bio;
	@Wire
	Label nameLabel;
	
	//services
	AuthenticationService authService = new AuthenticationServiceImpl();
	UserInfoService userInfoService = new UserInfoServiceImpl();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		ListModelList<String> countryModel = new ListModelList<String>(CommonInfoService.getCountryList());
		country.setModel(countryModel);
		
		refreshProfileView();
	}
	
	
	@Listen("onClick=#saveProfile")
	public void doSaveProfile(){
		UserCredential cre = authService.getUserCredential();
		User user = userInfoService.findUser(cre.getAccount());
		if(user==null){
			//TODO handle un-authenticated access 
			return;
		}
		
		//apply component value to bean
		user.setFullName(fullName.getValue());
		user.setEmail(email.getValue());
		user.setBirthday(birthday.getValue());
		user.setBio(bio.getValue());
		
		Set<String> selection = ((ListModelList)country.getModel()).getSelection();
		if(!selection.isEmpty()){
			user.setCountry(selection.iterator().next());
		}else{
			user.setCountry(null);
		}
		
		nameLabel.setValue(fullName.getValue());
		
		userInfoService.updateUser(user);
		
		Clients.showNotification("Your profile is updated");
	}
	
	@Listen("onClick=#reloadProfile")
	public void doReloadProfile(){
		refreshProfileView();
	}

	private void refreshProfileView() {
		UserCredential cre = authService.getUserCredential();
		User user = userInfoService.findUser(cre.getAccount());
		if(user==null){
			//TODO handle un-authenticated access 
			return;
		}
		
		//apply bean value to UI components
		account.setValue(user.getAccount());
		fullName.setValue(user.getFullName());
		email.setValue(user.getEmail());
		birthday.setValue(user.getBirthday());
		bio.setValue(user.getBio());
		
		((ListModelList)country.getModel()).addToSelection(user.getCountry());

		nameLabel.setValue(user.getFullName());
	}
}
