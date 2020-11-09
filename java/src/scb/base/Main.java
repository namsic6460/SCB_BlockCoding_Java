package scb.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import scb.base.Config.TRIBE;
import scb.base.Config.UNIT_TYPE;
import scb.form.StartForm;

public class Main {
	
	public static Thread initThread;
	
	public static void main(String[] args) {
		Cmd cmd = new Cmd();
		String command = cmd.inputCommand("set SCB_BlockCoding");
		String result = cmd.execCommand(command);
		
		if(result.equals("")) {
			String userName = System.getProperty("user.name");
			
			JFileChooser chooser = new JFileChooser(new File("C:/Users/" + userName + "/Desktop"));
			chooser.setFileFilter(new FileNameExtensionFilter("SCB_BlockCoding(Release) 파일", "exe"));
			
			int value;
			File selectedFile = null;
			while(true) {
				value = chooser.showOpenDialog(null);
				selectedFile = chooser.getSelectedFile();
				
				if(value == JFileChooser.APPROVE_OPTION && selectedFile.getName().equals("SCB_BlockCoding(Release).exe"))
					break;
			}
			
			String absPath = selectedFile.getAbsolutePath().replace("SCB_BlockCoding(Release).exe", "");
			
			StringBuilder builder = new StringBuilder("\"");
			builder.append(absPath);
			builder.append("src\\");
			command = cmd.inputCommand("setx SCB_BlockCoding " + builder.toString());
			cmd.execCommand(command);

			JOptionPane.showMessageDialog(null, "프로그램 설정을 완료했습니다!\n프로그램을 껐다 켜주세요");
			System.exit(0);
		}
		
		for(TRIBE tribe : TRIBE.values())
			Config.unitList.put(tribe, new LinkedHashMap<>());
		
		initThread = new Thread(new Runnable() {
			@Override
			public void run() {
				initUnits();
			}
		});
		
		try {
			Config.playSound("start.wav");
			initThread.start();
			new StartForm().setVisible(true);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
			for(JFrame frame : Config.frames)
				frame.dispose();
			return;
		}
	}
	
	private static void initUnits() {		
		Config.unitList.get(TRIBE.TERRAN).put(UNIT_TYPE.UNIT, new ArrayList<>(Arrays.asList(
				"SCV", "마린", "파이어뱃", "고스트", "메딕", "벌쳐", "탱크", "골리앗", "레이스", "드랍쉽", "사이언스 베슬", "배틀크루저", "발키리", "핵"
		)));
		ArrayList<String> strList = new ArrayList<>(Arrays.asList(
				"UnitType.Terran_SCV", "UnitType.Terran_Marine", "UnitType.Terran_Firebat", "UnitType.Terran_Ghost",
				"UnitType.Terran_Medic", "UnitType.Terran_Vulture", "UnitType.Terran_Siege_Tank_Tank_Mode",
				"UnitType.Terran_Goliath", "UnitType.Terran_Wraith", "UnitType.Terran_Dropship", "UnitType.Terran_Science_Vessel",
				"UnitType.Terran_Battlecruiser", "UnitType.Terran_Valkyrie", "UnitType.Terran_Nuclear_Missile"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.TERRAN).get(UNIT_TYPE.UNIT).get(i), strList.get(i));
		
		Config.unitList.get(TRIBE.TERRAN).put(UNIT_TYPE.BUILDING, new ArrayList<>(Arrays.asList(
				"커맨드 센터", "컴셋 스테이션", "뉴클리어 사일로", "서플라이 디팟", "리파이너리", "배럭", "엔지니어링 베이", "미사일 터렛", "아카데미", "벙커",
				"팩토리", "머신 샵", "아머리", "스타포트", "컨트롤 타워", "사이언스 퍼실리티", "피직스 랩", "커버트 옵스"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UnitType.Terran_Command_Center", "UnitType.Terran_Comsat_Station", "UnitType.Terran_Nuclear_Silo",
				"UnitType.Terran_Supply_Depot", "UnitType.Terran_Refinery", "UnitType.Terran_Barracks",
				"UnitType.Terran_Engineering_Bay", "UnitType.Terran_Missile_Turret", "UnitType.Terran_Academy",
				"UnitType.Terran_Bunker", "UnitType.Terran_Factory", "UnitType.Terran_Machine_Shop", "UnitType.Terran_Armory",
				"UnitType.Terran_Starport", "UnitType.Terran_Control_Tower", "UnitType.Terran_Science_Facility",
				"UnitType.Terran_Physics_Lab", "UnitType.Terran_Covert_Ops"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.TERRAN).get(UNIT_TYPE.BUILDING).get(i), strList.get(i));
		
		Config.unitList.get(TRIBE.TERRAN).put(UNIT_TYPE.UPGRADE, new ArrayList<>(Arrays.asList(
				"바이오닉 공격력", "바이오닉 방어력", "마린 사정거리", "메딕 마나", "고스트 시야", "고스트 마나" , "메카닉 지상 공격력", "메카닉 지상 방어력",
				"벌쳐 이동속도", "골리앗 공중 사정거리", "메카닉 공중 공격력", "메카닉 공중 방어력", "사이언스 베슬 마나", "배틀크루저 마나",
				"스파이더 마인", "시즈탱크", "고스트 락다운", "고스트 클로킹", "마린 스팀팩", "메딕 리스토레이션", "메딕 옵티컬 플레어",
				"레이스 클로킹", "배틀크루저 야마토 포"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UpgradeType.Terran_Infantry_Weapons", "UpgradeType.Terran_Infantry_Armor", "UpgradeType.U_238_Shells",
				"UpgradeType.Caduceus_Reactor", "UpgradeType.Ocular_Implants", "UpgradeType.Moebius_Reactor",
				"UpgradeType.Terran_Vehicle_Weapons", "UpgradeType.Terran_Vehicle_Plating", "UpgradeType.Ion_Thrusters",
				"UpgradeType.Charon_Boosters", "UpgradeType.Terran_Ship_Weapons", "UpgradeType.Terran_Ship_Plating",
				"UpgradeType.Titan_Reactor", "UpgradeType.Colossus_Reactor", "TechType.Spider_Mines",
				"TechType.Tank_Siege_Mode", "TechType.Lockdown", "TechType.Personnel_Cloaking", "TechType.Stim_Packs",
				"TechType.Restoration", "TechType.Optical_Flare", "TechType.Cloaking_Field", "TechType.Yamato_Gun"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.TERRAN).get(UNIT_TYPE.UPGRADE).get(i), strList.get(i));
		
		
		Config.unitList.get(TRIBE.PROTOSS).put(UNIT_TYPE.UNIT, new ArrayList<>(Arrays.asList(
				"프로브", "질럿", "드라군", "하이 템플러", "아칸", "다크 템플러", "다크 아칸", "셔틀", "리버", "옵저버", "스카웃", "캐리어", "인터셉터", "아비터", "커세어"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UnitType.Protoss_Probe", "UnitType.Protoss_Zealot", "UnitType.Protoss_Dragoon", "UnitType.Protoss_High_Templar",
				"UnitType.Protoss_Archon", "UnitType.Protoss_Dark_Templar", "UnitType.Protoss_Shuttle", "UnitType.Protoss_Reaver",
				"UnitType.Protoss_Observer", "UnitType.Protoss_Scout", "UnitType.Protoss_Carrier", "UnitType.Protoss_Interceptor",
				"UnitType.Protoss_Arbiter", "UnitType.Protoss_Corsair"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.PROTOSS).get(UNIT_TYPE.UNIT).get(i), strList.get(i));
		
		Config.unitList.get(TRIBE.PROTOSS).put(UNIT_TYPE.BUILDING, new ArrayList<>(Arrays.asList(
				"넥서스", "파일런", "어시밀레이터", "게이트웨이", "포지", "포톤 캐논", "사이버네틱스 코어", "실드 배터리", "로보틱스 퍼실리티", "스타게이트",
				"아둔", "서포트 베이", "플릿 비콘", "템플러 아카이브", "옵저버터리", "아비터 트리뷰널"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UnitType.Protoss_Nexus", "UnitType.Protoss_Pylon", "UnitType.Protoss_Gateway", "UnitType.Protoss_Forge",
				"UnitType.Protoss_Cybernetics_Core", "UnitType.Protoss_Shield_Battery", "UnitType.Protoss_Robotics_Facility",
				"UnitType.Protoss_Stargate", "UnitType.Protoss_Citadel_of_Adun", "UnitType.Protoss_Robotics_Support_Bay",
				"UnitType.Protoss_Fleet_Beacon", "UnitType.Protoss_Templar_Archives", "UnitType.Protoss_Observatory",
				"UnitType.Protoss_Arbiter_Tribunal"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.PROTOSS).get(UNIT_TYPE.BUILDING).get(i), strList.get(i));
		
		Config.unitList.get(TRIBE.PROTOSS).put(UNIT_TYPE.UPGRADE, new ArrayList<>(Arrays.asList(
				"지상 공격력", "지상 방어력", "실드", "질럿 이동속도", "드라군 사거리", "공중 공격력", "공중 방어력", "옵저버 시야", "옵저버 이동속도", "셔틀 이동속도",
				"리버 스캐럽 수용력", "리버 스캐럽 데미지", "스카웃 시야", "스카웃 이동속도", "커세어 마나", "캐리어 인터셉터 수용력", "하이템플러 마나", "아비터 마나", "다크아칸 마나",
				"사이오닉 스톰", "할루시네이션", "메일스트롬", "마인드컨트롤", "리콜", "스테이시스 필드"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UpgradeType.Protoss_Ground_Weapons", "UpgradeType.Protoss_Ground_Armor", "UpgradeType.Protoss_Plasma_Shields",
				"UpgradeType.Leg_Enhancements", "UpgradeType.Singularity_Charge", "UpgradeType.Protoss_Air_Weapons",
				"UpgradeType.Protoss_Air_Armor", "UpgradeType.Sensor_Array", "UpgradeType.Gravitic_Boosters",
				"UpgradeType.Gravitic_Drive", "UpgradeType.Reaver_Capacity", "UpgradeType.Scarab_Damage",
				"UpgradeType.Apial_Sensors", "UpgradeType.Gravitic_Thrusters", "UpgradeType.Argus_Jewel",
				"UpgradeType.Carrier_Capacity", "UpgradeType.Khaydarin_Amulet", "UpgradeType.Khaydarin_Core",
				"UpgradeType.Argus_Talisman", "TechType.Psionic_Storm", "TechType.Hallucination", "TechType.Mind_Control",
				"TechType.Maelstrom", "TechType.Recall", "TechType.Stasis_Field"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.PROTOSS).get(UNIT_TYPE.UPGRADE).get(i), strList.get(i));
		
		
		Config.unitList.get(TRIBE.ZERG).put(UNIT_TYPE.UNIT, new ArrayList<>(Arrays.asList(
				"드론", "오버로드", "저글링", "히드라리스크", "러커", "뮤탈리스크", "스콜지", "퀸", "가디언", "디바우러", "디파일러", "울트라리스크"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UnitType.Zerg_Drone", "UnitType.Zerg_Overlord", "UnitType.Zerg_Zergling", "UnitType.Zerg_Hydralisk",
				"UnitType.Zerg_Lurker", "UnitType.Zerg_Mutalisk", "UnitType.Zerg_Scourge", "UnitType.Zerg_Queen",
				"UnitType.Zerg_Guardian", "UnitType.Zerg_Devourer", "UnitType.Zerg_Ultralisk"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.ZERG).get(UNIT_TYPE.UNIT).get(i), strList.get(i));
		
		Config.unitList.get(TRIBE.ZERG).put(UNIT_TYPE.BUILDING, new ArrayList<>(Arrays.asList(
				"해처리", "레어", "하이브", "익스트랙터", "스포닝 풀", "에볼루션 챔버", "크립 콜로니", "스포어 콜로니", "성큰 콜로니", "히드라리스크 덴", "스파이어",
				"그레이터 스파이어", "퀸즈 네스트", "울트라리스크 캐번", "디파일러 마운드", "나이더스 커널"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UnitType.Zerg_Hatchery", "UnitType.Zerg_Lair", "UnitType.Zerg_Hive", "UnitType.Zerg_Extractor",
				"UnitType.Zerg_Spawning_Pool", "UnitType.Zerg_Evolution_Chamber", "UnitType.Zerg_Creep_Colony",
				"UnitType.Zerg_Spore_Colony", "UnitType.Zerg_Sunken_Colony", "UnitType.Zerg_Hydralisk_Den",
				"UnitType.Zerg_Spire", "UnitType.Zerg_Greater_Spire", "UnitType.Zerg_Queens_Nest", "UnitType.Zerg_Ultralisk_Cavern",
				"UnitType.Zerg_Defiler_Mound", "UnitType.Zerg_Nydus_Canal"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.ZERG).get(UNIT_TYPE.BUILDING).get(i), strList.get(i));
		
		Config.unitList.get(TRIBE.ZERG).put(UNIT_TYPE.UPGRADE, new ArrayList<>(Arrays.asList(
				"지상 근접 공격력", "지상 원거리 공격력", "지상 방어력", "저글링 공격속도", "저글링 이동속도", "히드라 이동속도", "히드라 사거리", "오버로드 수송", "오버로드 이동속도",
				"오버로드 시야", "공중 공격력", "공중 방어력", "퀸 마나", "디파일럿 마나", "울트라 이동속도", "울트라 방어력",
				"버로우", "러커 변이", "브루들링 소환", "인스네어", "플레이그", "컨슘"
		)));
		strList = new ArrayList<>(Arrays.asList(
				"UpgradeType.Zerg_Melee_Attacks", "UpgradeType.Zerg_Missile_Attacks", "UpgradeType.Zerg_Carapace",
				"UpgradeType.Adrenal_Glands", "UpgradeType.Metabolic_Boost", "UpgradeType.Muscular_Augments",
				"UpgradeType.Grooved_Spines", "UpgradeType.Ventral_Sacs", "UpgradeType.Pneumatized_Carapace",
				"UpgradeType.Antennae", "UpgradeType.Zerg_Flyer_Attacks", "UpgradeType.Zerg_Flyer_Carapace",
				"UpgradeType.Gamete_Meiosis", "UpgradeType.Metasynaptic_Node", "UpgradeType.Anabolic_Synthesis",
				"UpgradeType.Chitinous_Plating", "TechType.Burrowing", "TechType.Lurker_Aspect", "TechType.Spawn_Broodlings",
				"TechType.Ensnare", "TechType.Plague", "TechType.Consume"
		));
		for(int i = 0; i < strList.size(); i++)
			Config.toString.put(Config.unitList.get(TRIBE.ZERG).get(UNIT_TYPE.UPGRADE).get(i), strList.get(i));
	}
	
}
