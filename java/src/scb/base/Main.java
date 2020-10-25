package scb.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import scb.base.Config.TRIBE;
import scb.base.Config.UNIT_TYPE;
import scb.form.StartForm;

public class Main {
	
	public static Thread initThread;
	
	public static void main(String[] args) {
		for(TRIBE tribe : TRIBE.values())
			Config.unitList.put(tribe, new LinkedHashMap<>());
		
		initThread = new Thread(new Runnable() {
			@Override
			public void run() {
				initUnits();
			}
		});
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				new StartForm().setVisible(true);
			}
		});
		
		try {
			initThread.start();
			Config.playSound("start.wav");
			thread.start();
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);			
			for(JFrame frame : Config.frames)
				frame.dispose();
			return;
		}
	}
	
	private static void initUnits() {
		Config.unitList.get(TRIBE.TERRAN).put(UNIT_TYPE.UNIT, new ArrayList<>(Arrays.asList(
				"SCV", "마린", "파이어뱃", "고스트", "메딕", "벌쳐", "탱크", "골리앗", "레이스", "드랍쉽", "사이언스 베슬", "배틀크루저", "발키리", "핵"//전지수 바보
		)));
		Config.unitList.get(TRIBE.TERRAN).put(UNIT_TYPE.BUILDING, new ArrayList<>(Arrays.asList(
				"커맨드 센터", "컴셋 스테이션", "뉴클리어 사일로", "서플라이 디팟", "리파이너리", "배럭", "엔지니어링 베이", "미사일 터렛", "아카데미", "벙커",
				"팩토리", "머신 샵", "아머리", "스타포트", "컨트롤 타워", "사이언스 퍼실리티", "피직스 랩", "커버트 옵스"
		)));
		Config.unitList.get(TRIBE.TERRAN).put(UNIT_TYPE.UPGRADE, new ArrayList<>(Arrays.asList(
				"바이오닉 공격력", "바이오닉 방어력", "마린 사정거리", "메딕 마나", "고스트 시야", "고스트 마나" , "메카닉 지상 공격력", "메카닉 지상 방어력",
				"벌쳐 이동속도", "골리앗 공중 사정거리", "메카닉 공중 공격력", "메카닉 공중 방어력", "사이언스 베슬 마나", "배틀크루저 마나",
				"스파이더 마인", "시즈탱크", "고스트 락다운", "고스트 클로킹", "마린 스팀팩", "메딕 리스토레이션", "메딕 옵티컬 플레어",
				"레이스 클로킹", "배틀크루저 야마토 포"
		)));
		
		Config.unitList.get(TRIBE.PROTOSS).put(UNIT_TYPE.UNIT, new ArrayList<>(Arrays.asList(
				"프로브", "질럿", "드라군", "하이 템플러", "아칸", "다크 템플러", "다크 아칸", "셔틀", "리버", "옵저버", "스카웃", "캐리어", "인터셉터", "아비터", "커세어"
		)));
		Config.unitList.get(TRIBE.PROTOSS).put(UNIT_TYPE.BUILDING, new ArrayList<>(Arrays.asList(
				"넥서스", "파일런", "어시밀레이터", "게이트웨이", "포지", "포톤 캐논", "사이버네틱스 코어", "실드 배터리", "로보틱스 퍼실리티", "스타게이트",
				"아둔", "서포트 베이", "플릿 비콘", "템플러 아카이브", "옵저버터리", "아비터 트리뷰널"
		)));
		Config.unitList.get(TRIBE.PROTOSS).put(UNIT_TYPE.UPGRADE, new ArrayList<>(Arrays.asList(
				"지상 공격력", "지상 방어력", "실드", "질럿 이동속도", "드라군 사거리", "공중 공격력", "공중 방어력", "옵저버 시야", "옵저버 이동속도", "셔틀 이동속도",
				"리버 스캐럽 수용력", "리버 스캐럽 데미지", "스카웃 시야", "스카웃 이동속도", "커세어 마나", "캐리어 인터셉터 수용력", "하이템플러 마나", "아비터 마나", "다크아칸 마나",
				"사이오닉 스톰", "할루시네이션", "메일스트롬", "마인드컨트롤", "리콜", "스테이시스 필드"
		)));
		
		Config.unitList.get(TRIBE.ZERG).put(UNIT_TYPE.UNIT, new ArrayList<>(Arrays.asList(
				"드론", "오버로드", "저글링", "히드라리스크", "러커", "뮤탈리스크", "스콜지", "퀸", "가디언", "디바우러", "디파일러", "울트라리스크"
		)));
		Config.unitList.get(TRIBE.ZERG).put(UNIT_TYPE.BUILDING, new ArrayList<>(Arrays.asList(
				"해처리", "레어", "하이브", "익스트랙터", "스포닝 풀", "에볼루션 챔버", "크립 콜로니", "스포어 콜로니", "성큰 콜로니", "히드라리스크 덴", "스파이어",
				"그레이터 스파이어", "퀸즈 네스트", "울트라리스크 캐번", "디파일러 마운드", "나이더스 커널"
		)));
		Config.unitList.get(TRIBE.ZERG).put(UNIT_TYPE.UPGRADE, new ArrayList<>(Arrays.asList(
				"지상 근접 공격력", "지상 원거리 공격력", "지상 방어력", "저글링 공격속도", "저글링 이동속도", "히드라 이동속도", "히드라 사거리", "오버로드 수송", "오버로드 이동속도",
				"오버로드 시야", "공중 공격력", "공중 방어력", "퀸 마나", "디파일럿 마나", "울트라 이동속도", "울트라 방어력",
				"버로우", "러커 변이", "브루들링 소환", "인스네어", "플레이그", "컨슘"
		)));
	}
	
}
