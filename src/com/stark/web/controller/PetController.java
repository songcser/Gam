package com.stark.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stark.web.entity.PetInfo;
import com.stark.web.service.IPetManager;

@Controller
@RequestMapping("/pet")
public class PetController {

	@Resource(name = "petManager")
	private IPetManager petManager;

	@RequestMapping("/add.do")
	@ResponseBody
	public String add(@RequestBody PetInfo pInfo, HttpServletResponse response) {
		System.out.println("/pet/add?" + pInfo);
		String result = petManager.addPet(pInfo);
		String ret = "{\"result\":\"" + (result == null ? 1 : 0) + "\"}";

		return ret;
	}

	@RequestMapping("/update.do")
	@ResponseBody
	public Map<String, Object> update(@RequestBody PetInfo pInfo) {
		System.out.println("/pet/update?" + pInfo);
		boolean result = petManager.updatePet(pInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result ? 1 : 0);

		return map;
	}

	@RequestMapping("/getPetInfo.do")
	@ResponseBody
	public Map<String, Object> getById(int petId) {
		System.out.println("/pet/getPetInfo?petId=" + petId);
		PetInfo pInfo = petManager.getPet(petId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", pInfo.getName());
		map.put("type", pInfo.getType());
		map.put("sex", pInfo.getSex());
		map.put("brithday", pInfo.getBirthday());
		map.put("headPic", pInfo.getHeadPic());

		return map;
	}

	@RequestMapping("/getListByUserId.do")
	public void getListByUserId(String uid) {
		System.out.println("/pet/getListByUserId?uid=" + uid);
		ArrayList<PetInfo> pList = petManager.getPets(uid);
		System.out.println(pList.size());
	}
}
