package com.notes.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.notes.dao.UserDao;
import com.notes.entity.Notes;
import com.notes.entity.User;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserDao userDao;

	@RequestMapping("/addNotes")
	public String addNotes() {
		return "add_notes";
	}

	@RequestMapping("/viewNotes")
	public String viewNotes(HttpSession session, Model m) {
		User us = (User) session.getAttribute("userObj");
		List<Notes> notes = userDao.getNotesByUser(us);
		m.addAttribute("list", notes);
		return "view_notes";
	}

	@RequestMapping("/editNotes")
	public String editNotes(@RequestParam("id") int id, Model m) {
		Notes n = userDao.getNotesById(id);
		m.addAttribute("notes", n);
		return "edit_notes";
	}

	@RequestMapping(path = "/updateNotes", method = RequestMethod.POST)
	public String updateNnotes(@ModelAttribute Notes n, HttpSession session) {
		User us = (User) session.getAttribute("userObj");
		n.setUser(us);
		n.setDate(LocalDateTime.now().toString());
		System.out.println(n);
		userDao.updateNotes(n);
		session.setAttribute("msg", "Notes Update Sucessfully");
		return "redirect:/user/viewNotes";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {

		session.removeAttribute("userObj");
		session.setAttribute("msg", "logout secessfully");
		return "login";
	}

	@RequestMapping(path = "/saveNotes", method = RequestMethod.POST)
	public String saveNotes(@ModelAttribute Notes n, HttpSession session) {
		User us = (User) session.getAttribute("userObj");
		n.setDate(LocalDateTime.now().toString());
		n.setUser(us);
		userDao.saveNotes(n);
		session.setAttribute("msg", "Notes Added Sucessfully");
		return "redirect:/user/addNotes";
	}

	@RequestMapping("/deleteNotes")
	public String deleteNotes(@RequestParam("id") int id, HttpSession session) {
		userDao.deleteNotes(id);
		session.setAttribute("msg", "Notes Delete Sucessfully");
		return "redirect:/user/viewNotes";
	}

}
