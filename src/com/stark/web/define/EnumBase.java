package com.stark.web.define;

public class EnumBase {

	public enum ChartletType{
		Word("字帖",0),
		Picture("图贴",1),
		Bubble("气泡框",2),
		Dialogue("台词",3);
		
		
		private String name;
		private int index;
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		private ChartletType(String name,int index){
			this.setName(name);
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	public enum ChartletStatus{
		Normal("普通",0),
		Last("最新",1);
		
		private String name;
		private int index;
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		private ChartletStatus(String name,int index){
			this.setName(name);
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	public enum FileStatus{
		Normal("普通",0),
		Last("最新",1);
		
		private String name;
		private int index;
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		private FileStatus(String name,int index){
			this.setName(name);
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	public enum ActivityType {
		Banner("Banner", 0), 
		TopRecommend("置顶推荐", 1),
		Join("可参加",2),
		NoJoin("不可参加",3);

		private String name;
		private int index;

		private ActivityType(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public enum TagStatus {
		Normal("普通", 0), 
		Online("上线", 1);

		private String name;
		private int index;

		private TagStatus(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public enum ActivityStatus {
		OnLine("上线", 0), 
		OffLine("下线", 1),
		Delete("删除",2);

		private String name;
		private int index;

		private ActivityStatus(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static String getName(int index) {
			for (ActivityStatus status : ActivityStatus.values()) {
				if (status.getIndex() == index) {
					return status.name;
				}
			}
			return null;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public interface PetType {

	}

	public enum DogPetType {
		AoQuan("獒犬", 0), AoMu("澳牧", 1), AFuHan("阿富汗", 2), ALaSiJia("阿拉斯加", 3), AoDaliequan("奥达猎犬", 4), AiErLanGeng("爱尔兰梗", 5), ATuWanLieQuan("阿图瓦猎犬", 6), AiErLanShuiQuan(
				"爱尔兰水犬", 7), AZhaWaKeQuan("阿札瓦克犬", 8), ALaBoLingTiAiRuiGeSiQuan("阿拉伯灵缇艾瑞格斯犬", 9), ALanDuoAoQuan("阿兰多獒犬", 10), ADengMuNiuQuan("阿登牧牛犬", 11), AoZhouMuNiuQuan(
				"澳洲牧牛犬", 12), APengZeMuNiuQuan("阿彭则牧牛犬", 13), ATeNaDaoLieQuan("埃特那岛猎犬", 14), AiErLanLieLangQuan("爱尔兰猎狼犬", 15), AoFuNieZhiShiQuan("奥弗涅指示犬", 16), AiErLanXiaGuGeng(
				"爱尔兰峡谷梗", 17), AoDiLiHeiHeLieQuan("奥地利黑褐猎犬", 18), AiRuiGeZhiShiQuan("艾瑞格指示犬", 19), AiSiTeBeiLaShanQuan("埃斯特卑拉山犬", 20), ATeLaSiMuYangQuan(
				"阿特拉斯牧羊犬", 21), AoDaLiYaKaErBi("澳大利亚卡尔比", 22), AnNaTuoLiYaMuYangQuan("安纳托利亚牧羊犬", 23), AiErLanRuanMaoMaiSeGeng("爱尔兰软毛麦色梗", 24), AiErLanHongBaiDunLeiQuan(
				"爱尔兰红白蹲猎犬", 25), AoDaLiYaDuanMaoBinSaQuan("澳大利亚短毛宾莎犬", 26), ATiSangNuoManDiDuanTuiQuan("阿提桑诺曼底短腿犬", 27), AErBeiSiDaQieSiBoLaKeQuan(
				"阿尔卑斯达切斯勃拉克犬", 28),

		BoMei("博美", 29), BaGe("巴哥", 30), BianMu("边牧", 31), BoLi("波利", 32), BiTe("比特", 33), BiGe("比格", 34), BiXiong("比熊", 35), BaSiAo("巴西獒", 36), BoMiQuan(
				"波密犬", 37), BoEnShanBanDianGou("伯恩山斑点狗", 38), BaJieDuBoDeGeng("巴吉度伯德梗", 39), BiLiQuan("比利犬", 40), BaXiGeng("巴西梗", 41), BiaoZhunGuiBin("标准贵宾",
				42), BoShiDunGeng("波士顿梗", 43), BeiHaiDaoQuan("北海道犬", 44), BoLanLing("波兰灵", 45), TiBaXinJiQuan("缇巴辛吉犬", 46), BoLanLieQuan("波兰猎犬", 47), BeiLingDunGeng(
				"贝灵顿梗", 48), BiLiShiMuNiu("比利时牧牛", 49), BiLiShiLieQuan("比利时猎犬", 50), BoPangZhiShiQuan("波旁指示犬", 51), BoLieTaNiQuan("布列塔尼犬", 52), BingDaoMuYangQuan(
				"冰岛牧羊犬", 53), BiLuWuMaoQuan("秘鲁无毛犬", 54), BiLiShiMuYangQuan("比利时牧羊犬", 55), BiaoZhunChangMaoLaChang("标准长毛腊肠", 56), BiaoZhunGangMaoLaChang(
				"标准刚毛腊肠", 57), BuLaSaiErLieQuan("布拉塞尔猎犬", 58), BoSaWeiZiLieQuan("波萨维茨猎犬", 59), BoLunYaBanSuiQuan("波伦亚伴随犬", 60), BoGeSiZhiShiQuan("伯格斯指示犬", 61), BiLiNiuSiAoQuan(
				"比利牛斯獒犬", 62), BoLanDiDiMuYanQuan("波兰低地牧羊犬", 63), BeiJiaMaSiKaMuYangQuan("贝加马斯卡牧羊犬", 64), BoSiNiYaCuMaoLieQuan("波斯尼亚粗毛猎犬", 65), BaFaLiYanShanXiuLieQuan(
				"巴伐利亚山嗅猎犬", 66), BoXiMiYaYingMaoGeLiFenZhiShiQuan("波西米亚硬毛格里芬指示犬", 67),

		ChaiQuan("柴犬", 68), ChaLiWang("查理王", 69), CiQiQuan("瓷器犬", 70), ChuanChuanBig("串串大型", 71), ChuanChuanMedium("串串中型", 72), ChuanChuanSmall("串串小型", 73), ChaErSiWangXiaoLieQuan(
				"查尔斯王小猎犬", 74), CuMaoYiDaLiLieQuan("粗毛意大利猎犬", 75), ChangMaoBiLiNiuSiMuYangQuan("长毛比利牛斯牧羊犬", 76),

		DeMu("德牧", 77), DaDan("大丹", 78), DuGao("杜高", 79), DuBin("杜宾", 80), DaBaiXiong("大白熊", 81), DeGuoLieQuan("德国猎犬", 82), DouNiuAoQuan("斗牛獒犬", 83), DeGuoLieGeng(
				"德国猎梗", 84), DuoMaoMuYangQuan("短毛牧羊犬", 85), DaGeLinFenQuan("大格林芬犬", 86), DuanJiaoChangShenGen("短脚长身梗", 87), DeGuoBinSaQuan("德国宾莎犬", 88),

		EGuoLingTi("俄国灵缇", 89), EnTeBuShanDiQuan("恩特布山地犬", 90), ELuoSiNanBuMuYangQuan("俄罗斯南部牧羊犬", 91),

		FaGuoLangQuan("法国狼犬", 92), FaGuoDouNiu("法国斗牛", 93), FenLanHuQuan("芬兰狐犬", 94), FaGuoShuiQuan("法国水犬", 95), FaGuoLieQuan("法国猎犬", 96), FenLanLieQuan(
				"芬兰猎犬", 97), FenLanXunLuQuan("芬兰驯鹿犬", 98), FaLaoWangLieQuan("法老王猎犬", 99), FaGuoSanSeLieQuan("法国三色猎犬", 100), FuRuiSiAnShuiQuan("佛瑞斯安水犬", 101), FenLanLaPuLieQuan(
				"芬兰拉普猎犬", 102), FaGuoBoErDuoAo("法国波尔多獒", 103),

		GuMu("古牧", 104), GuanMaoGeLingLanQuan("冠毛格陵兰犬", 104), GangMaoLieHuGeng("刚毛猎狐梗", 105), GeDengDunLieQuan("戈登蹲猎犬", 106), GaoJiaSuoMuYangQuan("高加索牧羊犬", 107), GeLiFenNiWeiNaiQuan(
				"格里芬尼韦奈犬", 108),

		HuDie("蝴蝶", 109), HouMianGeng("猴面梗", 110), HaShiQi("哈士奇", 111), HuPanGeng("湖畔梗", 112), HuiBiTe("惠比特", 113), HaiGenLieQuan("海根猎犬", 114), HeLanLieNiaoQuan(
				"荷兰猎鸟犬", 115), HanGuoJinDao("韩国金刀", 116), QuanHaErDengLieQuan("犬哈尔登猎犬", 117), ELuoSiHeiGeng("俄罗斯黑梗", 118), HeLanMuYangQuan("荷兰牧羊犬", 119), HeiHeLieHuanXiongQuan(
				"黑褐猎浣熊犬", 120),

		JingBa("京巴", 121), JinMao("金毛", 122), JieKeGengJiaNanQuan("捷克梗迦南犬", 123), JiZhouQuanJiaFeiQuan("纪州犬甲斐犬", 124), JiWaWaJieKeLangQuan("吉娃娃捷克狼犬", 125), JiaNaLiAoJuanMaoLangQuan(
				"加那利獒卷毛指示犬", 126), JuXingXueNaRui("巨型雪纳瑞", 127), JuanMaoXunHuiLieQuan("卷毛寻回猎犬", 128),

		KeJi("柯基", 129), KaiEnGeng("凯恩梗", 130), KeMengQuan("可蒙犬", 131), KuWaZiQuanKaiLiLanGeng("库瓦兹犬凯利蓝埂", 132), KeLunBoLieQuan("克伦伯猎犬", 133), KaDiGenKeJi(
				"卡迪根柯基", 134), KeSiTeMuYangQuan("卡斯特牧羊犬", 135), KeLongFuLanDeQuan("克龙弗兰德犬", 136), KaLeiLiYaXiong("卡累利亚熊犬", 137), KaTaLanMuYangQuan("卡塔兰牧羊犬",
				138), KeLouDiYaMuYangQuan("克罗地亚牧羊犬", 139),

		LingTi("灵缇", 140), LuoFuGeng("罗福梗", 141), LuoWeiNa("罗威纳", 142), LieTuQuan("猎兔犬", 143), LieLuQuan("猎鹿犬", 144), LaSaQuan("拉萨犬", 145), LaBuLaDuo("拉布拉多",
				146), LanXiErQuan("兰西尔犬", 147), LanBoGeQuan("兰波格犬", 148), LaKanNuoSiQuan("拉坎诺斯犬", 149), LuoManNaShuiQuan("罗曼娜水犬", 150), BiaoZhunDuanMaoLanCha(
				"标准短毛腊肠", 151),

		MaDiQuan("马地犬", 152), MiNiPin("迷你品", 153), MeiGuoKeKaMiNiGuiBin("美国可卡迷你贵宾", 154), MaLueKaAo("马略卡獒", 155), MaErJiSi("马尔济斯", 156), MiNiXueNaRui("迷你雪纳瑞",
				157), NaLiNuoSiQuan("马利诺斯犬", 158), MeiGuoLieHuQuan("美国猎狐犬", 159), MeiGuoShuiLieQuan("美国水猎犬", 160), MeiGuoQiuTianQuan("美国秋田犬", 161), MianHuaMianShaQuan(
				"棉花面纱犬", 162),

		NuoWeiLieQuan("挪威猎犬", 163), NiuFenLanQuan("纽芬兰犬", 164), NuoWeiCiGengNiuTouGeng("诺维茨梗牛头梗", 165), NuoWeiMuYangQuan("挪威牧羊犬", 166), NaBuLeSiAo("那不勒斯獒", 167), NuoWeiLieLuQuan(
				"挪威猎鹿犬", 168), NuoBoDanHuLiQuan("诺波丹狐狸犬", 169), NuoWeiLuDeHangQuan("挪威卢德杭犬", 170),

		OYaDaLuQuan("欧亚大陆犬", 171), QShiEGuoLaiKaQuan("欧式俄国莱卡犬", 172),

		PeiDiFenQuan("佩狄芬犬", 173), PingMaoXunLieQuan("平毛寻猎犬", 174), PuTaoYaShuiQuan("葡萄牙水犬", 175), PiKaDiLieQuan("皮卡第猎犬", 176), PingMaoLieHuGeng("平毛猎狐梗", 177), PuTaoYaZhiShiQuan(
				"葡萄牙指示犬", 178),

		QiuTian("秋田", 179), QuanShi("拳师", 180), QieSaPiKeWanXunLieQuan("切萨皮克湾寻猎犬", 181), QianHuangBuLieTaNiDuanTuiQuan("浅黄不列塔尼短腿犬", 182), QianHuangBuLieTaNiGeLiFenQuan(
				"浅黄布列塔尼格里芬犬", 183),

		RiBenGeng("日本梗", 184), RiBenDiRiShiLieQuan("日本狆瑞士猎犬", 185), RiBenJianZui("日本尖嘴", 186), RuiDianLaChangQuan("瑞典腊肠犬", 187), RuiDianLieLuQuan("瑞典猎鹿犬", 188), RuiDianKeJiQuan(
				"瑞典柯基犬", 189), RuiDianLaPuLieQuan("瑞典拉普猎犬", 190),

		ShaPi("沙皮", 191), SuMu("苏牧", 192), SongShi("松狮", 193), SaMoYe("萨摩耶", 194), SiGuoQuan("四国犬", 195), SiKaiGeng("斯凯梗", 196), ShengBoNa("圣伯纳", 197), SiMaoGeng(
				"丝毛梗", 198), SuGeLanGeng("苏格兰梗", 199), SuELieLangQuan("苏俄猎狼犬", 200), SaLuJiLieQuan("萨卢基猎犬", 201), SiTaBiHeLieQuan("斯塔比荷猎犬", 202),

		TuZuoQuan("土佐犬", 203), TaiWanQuan("台湾犬", 204), TianYeLieQuan("田野猎犬", 205), TaiGuoJiBeiQuan("泰国脊背犬", 206), TiLuoErQuan("提洛尔猎犬", 207), TuXingGangMaoLaChuang(
				"兔型刚毛腊肠", 208), TuXingChangMaoLaChuang("兔型长毛腊肠", 209), TeWuLunMuYangQuan("特武伦牧羊犬", 210), TuXingDuanMaoLaChuang("兔型短毛腊肠", 211), TaiTuoLaMuYangQuan(
				"泰托拉牧羊犬", 212), TeLanXiWaNiYaLieQuan("特兰西瓦尼亚猎犬", 213),

		WanNengGeng("万能梗", 214), WeiMaLieQuan("魏玛猎犬", 215), WanJuGuiBing("玩具贵宾", 216), WeiErShiGeng("威尔士梗", 217), WeiErShiShiBingGe("威尔士史宾格", 218), WeiSiTeFaLiYaDaQieSiBeLaKeQuan(
				"威斯特伐利亚·达切斯勃拉克犬", 219),

		XiShi("西施", 220), XiZangGeng("西藏梗", 221), XiLeDi("喜乐蒂", 222), XueNaRui("雪纳瑞", 223), XiLeLieQuan("席勒猎犬", 224), XiZangLieQuan("西藏猎犬", 225), XunXueLieQuan(
				"寻血猎犬", 226), XiaoShiZiGou("小狮子狗", 227), XiLaLieQuan("希腊猎犬", 228), XiBanYaAoQuan("西班牙獒犬", 229), XiBanYaShuiQuan("西班牙水犬", 230), XiaoRuiShiQuan(
				"小瑞士猎犬", 231), XiBanYaLieQuan("西班牙猎犬", 232), XiBanYaLingTi("西班牙灵缇", 233), XiongYaLiLingTi("匈牙利灵缇", 234), XiXiLiLieQuan("西西里猎犬", 235), XiLiHanMuGeng(
				"西里汉姆梗", 236), XiGaoDIBaiGeng("西高地白梗", 237),

		YueKeXia("约克夏", 238), YingGuoDouNiu("英国斗牛", 239), YingGuoKeYin("英国可卡", 240), YingFaXiaoLieQuan("英法小猎犬", 241), YiDaLiLingTi("意大利灵缇", 242), YingGuoZhiShiQuan(
				"英国指示犬", 243), YingGuoLieHuQuan("英国猎狐犬", 244), YingGuoWanJuGeng("英国玩具梗", 245), YingGuoShiBingGe("英国史宾格", 246), YiDaLiZhiShiQuan("意大利指示犬", 247),

		ZangAo("藏獒", 248), ZhongXingGuiBing("中型贵宾", 249), ZhongYaMuYangQuan("中亚牧羊犬", 250), ZhongXingGeLiFenQuan("中型格里芬狩猎犬", 251);

		private String name;
		private int index;

		private DogPetType(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public static String getName(int index) {
			for (DogPetType type : DogPetType.values()) {
				if (type.getIndex() == index) {
					return type.name;
				}
			}
			return null;
		}

		public static int getIndex(String name) {
			for (DogPetType type : DogPetType.values()) {
				if (type.getName() == name) {
					return type.getIndex();
				}
			}
			return -1;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public enum CatPetType {
		HuBanMao("虎斑猫", 501), HeiZuMao("黑足猫", 502), ManChiKenMao("曼赤肯猫", 503), ShangDongShiZiMao("山东狮子猫", 504), SiChuanJianZhouMao("四川简州猫", 505), ZhongGuoYunMao(
				"中国云猫", 506), JinJiLa("金吉拉", 507), HongSeZhongDianSeBoSiMao("红色重点色波斯猫", 508), HongHuBanYiGuoDuanMaoMao("红虎斑异国短毛猫", 509), AiQingMao("爱琴猫", 510), AoDaLiYaWuMao(
				"澳大利亚雾猫", 511), MeiGuoDuoZhiMao("美国多趾猫", 512), MeiGuoDuanWeiMao("美国短尾猫", 513), MeiGuoJuanWeiMao("美国卷耳猫", 514), MeiGuoJuanMaoMao("美国卷毛猫", 515), MeiGuoDuanMaoMao(
				"美国短毛猫", 516), MeiGuoGangMaoMao("美国钢毛猫", 517), DiFaNiMao("蒂法尼猫", 518), BaLiMao("巴厘猫", 519), MengJiaLaMao("孟加拉猫", 520), ABiXiNiYaMao("阿比西尼亚猫",
				521), AiJiMao("埃及猫", 522), AoXiMao("奥西猫", 523), BoManMao("伯曼猫", 524), BuOuMao("布偶猫", 525), BoSiMao("波斯猫", 526), DeWenJuanMaoMao("德文卷毛猫", 527), DianTangJuanMao(
				"电烫卷猫", 528), DongFangDuanMaoMao("东方短毛猫", 529), DongQiNiMao("东奇尼猫", 530), ELuoSiLanMao("俄罗斯蓝猫", 531), HaWaNaZongMao("哈瓦那棕猫", 532), JiaNaDaMao(
				"加拿大无毛猫", 533), KeNiSiMao("柯尼斯卷毛猫", 534), KeLaTeMao("科拉特猫", 535), lanLuMao("褴褛猫", 536), MenMaiMao("孟买猫", 537), MianDianMao("缅甸猫", 538), MianYinMao(
				"缅因猫", 539), ManDaoWuWeiMao("曼岛无尾猫", 540), NuoWeiSengLinMao("挪威森林猫", 541), RiBenDuanWeiMao("日本短尾猫", 542), SiKeKeMao("斯可可猫", 543), SuoMaLiMao(
				"索马里猫", 544), TuErQiAnGeLaMao("土耳其安哥拉猫", 545), TuErQiFanMao("土耳其梵猫", 546), XiaTeErMao("夏特尔猫", 547), SuGeLanZheErMao("苏格兰折耳猫", 548), MaEnDaoMao(
				"马恩岛猫", 549);

		private String name;
		private int index;

		private CatPetType(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public static String getName(int index) {
			for (CatPetType type : CatPetType.values()) {
				if (type.getIndex() == index) {
					return type.name;
				}
			}
			return null;
		}

		public static int getIndex(String name) {
			for (CatPetType type : CatPetType.values()) {
				if (type.getName() == name) {
					return type.getIndex();
				}
			}
			return -1;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public enum OtherPetType {
		ChuiErTu("垂耳兔", 700), AnGeLaTu("安哥拉兔", 701), ShiZiTu("狮子兔", 702), ZhuRuTu("侏儒兔", 703), HuaMingTu("花明兔", 704), ZiCang("紫仓", 705), SanXianCangShu("三线仓鼠",
				706), XiaongMao("熊猫", 707), HeiXianCangShu("黑线仓鼠", 708), SongShuHuaShu("松鼠花鼠", 709), MiDaiYan("蜜袋鼯", 710), WaYanShouGang("蛙眼守宫", 711), HaiNanShouGang(
				"海南守宫", 712), BaoWenShouGang("豹纹守宫", 713), LvLieXi("绿鬣蜥", 714), PingYuan("平原", 715), TaiJia("泰加", 716), BianSeLong("变色龙", 717), BaXiGui("巴西龟",
				718), CaoGui("草龟", 719), HuangHouNiShuiGui("黄喉拟水龟", 720), ZhenZhuGui("珍珠龟", 721), FeiHeGui("飞河龟", 722), LuGui("陆龟", 723);

		private String name;
		private int index;

		private OtherPetType(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public static String getName(int index) {
			for (OtherPetType type : OtherPetType.values()) {
				if (type.getIndex() == index) {
					return type.name;
				}
			}
			return null;
		}

		public static int getIndex(String name) {
			for (OtherPetType type : OtherPetType.values()) {
				if (type.getName() == name) {
					return type.getIndex();
				}
			}
			return -1;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public enum FriendStatus {
		Request("请求", 0), Agree("同意", 1), Deny("拒绝", 2), Resist("屏蔽", 3);
		private String name;
		private int index;

		private FriendStatus(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (FriendStatus status : FriendStatus.values()) {
				if (status.getIndex() == index) {
					return status.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

	}

	public enum NoticeType {
		Follow("关注", 0), 
		Comment("评论", 1), 
		Praise("赞", 2), 
		System("系统", 3), 
		At("@", 4);

		private String name;
		private int index;

		private NoticeType(String name, int index) {
			this.name = name;
			this.setIndex(index);
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public static String getName(int index) {
			for (NoticeType type : NoticeType.values()) {
				if (type.getIndex() == index) {
					return type.name;
				}
			}
			return null;
		}
	}

	public enum NoticeStatus {
		NoRead("未读", 0), 
		Readed("已读", 1);

		private String name;
		private int index;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		private NoticeStatus(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public static String getName(int index) {
			for (NoticeStatus status : NoticeStatus.values()) {
				if (status.getIndex() == index) {
					return status.name;
				}
			}
			return null;
		}
	}

	public enum Sex {
		male("男性", 0), female("女性", 1),unKnow("未知",2);

		private String name;
		private int index;

		private Sex(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

	}

	public enum UserRole {
		Normal("普通用户", 0), 
		Admin("管理员", 1), 
		//mustFollow("必须关注", 2),
		Operatior("运营", 2),
		Simulation("模拟用户",3),
		MustFollow("必须关注", 4),
		Recommend("推荐关注", 5),
		Important("重要用户",6),
		Mark("标记用户",7),
		Organization("机构用户",8);

		private String name;
		private int index;

		private UserRole(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
		
		public static String getName(int index) {
			for (UserRole status : UserRole.values()) {
				if (status.getIndex() == index) {
					return status.name;
				}
			}
			return null;
		}
	}

	public enum UserStatus {
		OnLine("在线", 0), 
		OffLine("离线", 1), 
		Invisible("隐身", 2);

		private String name;
		private int index;

		private UserStatus(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public enum ArticleStatus {
		Normal("正常", 0), Delete("删除", 1), Invisible("不可见", 2);

		private String name;
		private int index;

		private ArticleStatus(String name, int index) {
			this.setName(name);
			this.setIndex(index);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

	}

	public enum ArticleType {
		Publish("普通", 0),
		Forward("转发", 1), 
		DayExquisite("推荐", 2), 
		FashionMagazine("杂志", 3), 
		ExquisiteMagazine("精选杂志", 4), 
		Report("举报", 5),
		DayExquisiteReport("推荐被举报",6),
		FashionMagazineReport("杂志被举报",7),
		ExquisiteMagazineReport("推荐杂志被举报",8),
		NoAuditingActivity("未审核节目单推文",9),
		Activity("节目单推文",10),
		Delete("已删除",11),
		ExquisiteNoAuditing("推荐未审核",12),
		CommonExquisite("普通推荐",13),
		ActivityExquisite("节目单推荐",14),
		CommonNoAuditing("普通未审核",15),
		CommonActivity("普通节目单",16),
		CommonActivityExquisite("普通节目单推荐",17);

		private String name;
		private int index;

		private ArticleType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public static String getName(int index) {
			for (ArticleType type : ArticleType.values()) {
				if (type.getIndex() == index) {
					return type.name;
				}
			}
			return null;
		}
	}

	public enum BackupStatus{
		Runing("运行中",1),
		Stoped("停止",0);
		
		private int index;
		private String name;
		
		private BackupStatus(String name,int index){
			this.setName(name);
			this.setIndex(index);
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	public enum BackupFrequency{
		EveryDay("每天",0),
		EveryWeek("每周",1),
		EveryMonth("每月",2);
		
		private int index;
		private String name;
		
		private BackupFrequency(String name,int index){
			this.name = name;
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
