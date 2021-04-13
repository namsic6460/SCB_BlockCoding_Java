package scb.basicbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;

import bwapi.Race;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import scb.base.Config;
import scb.base.Config.TRIBE;
import scb.base.Config.UNIT_TYPE;

/// 상황을 판단하여, 정찰, 빌드, 공격, 방어 등을 수행하도록 총괄 지휘를 하는 class <br>
/// InformationManager 에 있는 정보들로부터 상황을 판단하고, <br>
/// BuildManager 의 buildQueue에 빌드 (건물 건설 / 유닛 훈련 / 테크 리서치 / 업그레이드) 명령을 입력합니다.<br>
/// 정찰, 빌드, 공격, 방어 등을 수행하는 코드가 들어가는 class
public class StrategyManager {

	private static StrategyManager instance = new StrategyManager();

	private CommandUtil commandUtil = new CommandUtil();

	private boolean isFullScaleAttackStarted;
	
	/// static singleton 객체를 리턴합니다
	public static StrategyManager Instance() {
		return instance;
	}

	public StrategyManager() {
		isFullScaleAttackStarted = false;
	}

	/// 경기가 시작될 때 일회적으로 전략 초기 세팅 관련 로직을 실행합니다
	public void onStart() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(MyBotModule.Broodwar.getFrameCount() < 1)
						continue;
					
					ArrayList<Object> objList = new ArrayList<>(Arrays.asList(
							UnitType.Terran_SCV, UnitType.Terran_Marine, UnitType.Terran_Firebat, UnitType.Terran_Ghost,
							UnitType.Terran_Medic, UnitType.Terran_Vulture, UnitType.Terran_Siege_Tank_Tank_Mode,
							UnitType.Terran_Goliath, UnitType.Terran_Wraith, UnitType.Terran_Dropship, UnitType.Terran_Science_Vessel,
							UnitType.Terran_Battlecruiser, UnitType.Terran_Valkyrie, UnitType.Terran_Nuclear_Missile
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.TERRAN).get(UNIT_TYPE.UNIT).get(i), objList.get(i));
					
					objList = new ArrayList<>(Arrays.asList(
							UnitType.Terran_Command_Center, UnitType.Terran_Comsat_Station, UnitType.Terran_Nuclear_Silo,
							UnitType.Terran_Supply_Depot, UnitType.Terran_Refinery, UnitType.Terran_Barracks,
							UnitType.Terran_Engineering_Bay, UnitType.Terran_Missile_Turret, UnitType.Terran_Academy,
							UnitType.Terran_Bunker, UnitType.Terran_Factory, UnitType.Terran_Machine_Shop, UnitType.Terran_Armory,
							UnitType.Terran_Starport, UnitType.Terran_Control_Tower, UnitType.Terran_Science_Facility,
							UnitType.Terran_Physics_Lab, UnitType.Terran_Covert_Ops
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.TERRAN).get(UNIT_TYPE.BUILDING).get(i), objList.get(i));
					
					objList = new ArrayList<>(Arrays.asList(
							UpgradeType.Terran_Infantry_Weapons, UpgradeType.Terran_Infantry_Armor, UpgradeType.U_238_Shells,
							UpgradeType.Caduceus_Reactor, UpgradeType.Ocular_Implants, UpgradeType.Moebius_Reactor,
							UpgradeType.Terran_Vehicle_Weapons, UpgradeType.Terran_Vehicle_Plating, UpgradeType.Ion_Thrusters,
							UpgradeType.Charon_Boosters, UpgradeType.Terran_Ship_Weapons, UpgradeType.Terran_Ship_Plating,
							UpgradeType.Titan_Reactor, UpgradeType.Colossus_Reactor, TechType.Spider_Mines,
							TechType.Tank_Siege_Mode, TechType.Lockdown, TechType.Personnel_Cloaking, TechType.Stim_Packs,
							TechType.Restoration, TechType.Optical_Flare, TechType.Cloaking_Field, TechType.Yamato_Gun
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.TERRAN).get(UNIT_TYPE.UPGRADE).get(i), objList.get(i));
					
					
					objList = new ArrayList<>(Arrays.asList(
							UnitType.Protoss_Probe, UnitType.Protoss_Zealot, UnitType.Protoss_Dragoon, UnitType.Protoss_High_Templar,
							UnitType.Protoss_Archon, UnitType.Protoss_Dark_Templar, UnitType.Protoss_Shuttle, UnitType.Protoss_Reaver,
							UnitType.Protoss_Observer, UnitType.Protoss_Scout, UnitType.Protoss_Carrier, UnitType.Protoss_Interceptor,
							UnitType.Protoss_Arbiter, UnitType.Protoss_Corsair
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.PROTOSS).get(UNIT_TYPE.UNIT).get(i), objList.get(i));
					
					objList = new ArrayList<>(Arrays.asList(
							UnitType.Protoss_Nexus, UnitType.Protoss_Pylon, UnitType.Protoss_Gateway, UnitType.Protoss_Forge,
							UnitType.Protoss_Cybernetics_Core, UnitType.Protoss_Shield_Battery, UnitType.Protoss_Robotics_Facility,
							UnitType.Protoss_Stargate, UnitType.Protoss_Citadel_of_Adun, UnitType.Protoss_Robotics_Support_Bay,
							UnitType.Protoss_Fleet_Beacon, UnitType.Protoss_Templar_Archives, UnitType.Protoss_Observatory,
							UnitType.Protoss_Arbiter_Tribunal
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.PROTOSS).get(UNIT_TYPE.BUILDING).get(i), objList.get(i));
					
					objList = new ArrayList<>(Arrays.asList(
							UpgradeType.Protoss_Ground_Weapons, UpgradeType.Protoss_Ground_Armor, UpgradeType.Protoss_Plasma_Shields,
							UpgradeType.Leg_Enhancements, UpgradeType.Singularity_Charge, UpgradeType.Protoss_Air_Weapons,
							UpgradeType.Protoss_Air_Armor, UpgradeType.Sensor_Array, UpgradeType.Gravitic_Boosters,
							UpgradeType.Gravitic_Drive, UpgradeType.Reaver_Capacity, UpgradeType.Scarab_Damage,
							UpgradeType.Apial_Sensors, UpgradeType.Gravitic_Thrusters, UpgradeType.Argus_Jewel,
							UpgradeType.Carrier_Capacity, UpgradeType.Khaydarin_Amulet, UpgradeType.Khaydarin_Core,
							UpgradeType.Argus_Talisman, TechType.Psionic_Storm, TechType.Hallucination, TechType.Mind_Control,
							TechType.Maelstrom, TechType.Recall, TechType.Stasis_Field
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.PROTOSS).get(UNIT_TYPE.UPGRADE).get(i), objList.get(i));
					
					
					objList = new ArrayList<>(Arrays.asList(
							UnitType.Zerg_Drone, UnitType.Zerg_Overlord, UnitType.Zerg_Zergling, UnitType.Zerg_Hydralisk,
							UnitType.Zerg_Lurker, UnitType.Zerg_Mutalisk, UnitType.Zerg_Scourge, UnitType.Zerg_Queen,
							UnitType.Zerg_Guardian, UnitType.Zerg_Devourer, UnitType.Zerg_Ultralisk
					));
					for(int i = 0; i < objList.size(); i++) 
						Config.toObject.put(Config.unitList.get(TRIBE.ZERG).get(UNIT_TYPE.UNIT).get(i), objList.get(i));
					
					objList = new ArrayList<>(Arrays.asList(
							UnitType.Zerg_Hatchery, UnitType.Zerg_Lair, UnitType.Zerg_Hive, UnitType.Zerg_Extractor,
							UnitType.Zerg_Spawning_Pool, UnitType.Zerg_Evolution_Chamber, UnitType.Zerg_Creep_Colony,
							UnitType.Zerg_Spore_Colony, UnitType.Zerg_Sunken_Colony, UnitType.Zerg_Hydralisk_Den,
							UnitType.Zerg_Spire, UnitType.Zerg_Greater_Spire, UnitType.Zerg_Queens_Nest, UnitType.Zerg_Ultralisk_Cavern,
							UnitType.Zerg_Defiler_Mound, UnitType.Zerg_Nydus_Canal
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.ZERG).get(UNIT_TYPE.BUILDING).get(i), objList.get(i));
					
					objList = new ArrayList<>(Arrays.asList(
							UpgradeType.Zerg_Melee_Attacks, UpgradeType.Zerg_Missile_Attacks, UpgradeType.Zerg_Carapace,
							UpgradeType.Adrenal_Glands, UpgradeType.Metabolic_Boost, UpgradeType.Muscular_Augments,
							UpgradeType.Grooved_Spines, UpgradeType.Ventral_Sacs, UpgradeType.Pneumatized_Carapace,
							UpgradeType.Antennae, UpgradeType.Zerg_Flyer_Attacks, UpgradeType.Zerg_Flyer_Carapace,
							UpgradeType.Gamete_Meiosis, UpgradeType.Metasynaptic_Node, UpgradeType.Anabolic_Synthesis,
							UpgradeType.Chitinous_Plating, TechType.Burrowing, TechType.Lurker_Aspect, TechType.Spawn_Broodlings,
							TechType.Ensnare, TechType.Plague, TechType.Consume
					));
					for(int i = 0; i < objList.size(); i++)
						Config.toObject.put(Config.unitList.get(TRIBE.ZERG).get(UNIT_TYPE.UPGRADE).get(i), objList.get(i));
					
					JComboBox<String> comboBox;
					for(int i = 0; i < 5; i++) {
						comboBox = Config.comboBoxes.get(i);
						if(comboBox.getSelectedIndex() == -1)
							continue;
						
						String text = comboBox.getSelectedItem().toString();
						int count = Integer.parseInt(Config.numLabels.get(i).getText());
						
						Config.attackTiming.put((UnitType) Config.toObject.get(text), count);
					}
					
					setInitialBuildOrder();
					break;
				}
			}
		}).start();;
	}

	///  경기가 종료될 때 일회적으로 전략 결과 정리 관련 로직을 실행합니다
	public void onEnd(boolean isWinner) {
	}

	/// 경기 진행 중 매 프레임마다 경기 전략 관련 로직을 실행합니다
	public void update() {
		if(MyBotModule.Broodwar.getFrameCount() % 4 == 0)
			executeCombat();
	}

	public void setInitialBuildOrder() {
		Race race = Race.Unknown;
		switch(Config.tribe) {
		case PROTOSS:
			race = Race.Protoss;
			break;
		case TERRAN:
			race = Race.Terran;
			break;
		case ZERG:
			race = Race.Zerg;
			break;
		}
		
		if(MyBotModule.Broodwar.self().getRace() == race) {
			for(JButton button : Config.buildList) {
				Object object = Config.toObject.get(button.getText());
				
				if(object instanceof UnitType) 
					BuildManager.Instance().buildQueue.queueAsLowestPriority((UnitType) object);
				if(object instanceof TechType)
					BuildManager.Instance().buildQueue.queueAsLowestPriority((TechType) object);
				if(object instanceof UpgradeType)
					BuildManager.Instance().buildQueue.queueAsLowestPriority((UpgradeType) object);
			}
		}
		
		else
			System.err.println("Selected race is different to the coding race");
	}
	
	public void executeCombat() {

		// 공격 모드가 아닐 때에는 전투유닛들을 아군 진영 길목에 집결시켜서 방어
		if (isFullScaleAttackStarted == false) {
//			Chokepoint firstChokePoint = BWTA.getNearestChokepoint(InformationManager.Instance().getMainBaseLocation(InformationManager.Instance().selfPlayer).getTilePosition());
			Chokepoint firstChokePoint = BWTA.getNearestChokepoint(InformationManager.Instance().getFirstExpansionLocation(InformationManager.Instance().selfPlayer).getPoint());
			
			for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
				if (unit.getType() == InformationManager.Instance().getBasicCombatUnitType() && unit.isIdle()) {
					boolean flag = false;
					Unit enemy = null;
					
					for(Unit u : unit.getUnitsInRadius(Math.max(unit.getType().groundWeapon().maxRange(), 
																unit.getType().airWeapon().maxRange()))) {
						if(u.getPlayer().equals(InformationManager.Instance().enemyPlayer)) {
							flag = true;
							enemy = u;
							break;
						}
					}
					
					if(!flag)
						commandUtil.attackMove(unit, firstChokePoint.getCenter());
					else
						commandUtil.attackMove(unit, enemy.getPoint());
				}
			}
			
			checkAttack();
		}
		// 공격 모드가 되면, 모든 전투유닛들을 적군 Main BaseLocation 로 공격 가도록 합니다
		else {
			//std.cout + "enemy OccupiedBaseLocations : " + InformationManager.Instance().getOccupiedBaseLocations(InformationManager.Instance()._enemy).size() + std.endl;
			
			if (InformationManager.Instance().enemyPlayer != null
					&& InformationManager.Instance().enemyRace != Race.Unknown 
					&& InformationManager.Instance().getOccupiedBaseLocations(InformationManager.Instance().enemyPlayer).size() > 0) 
			{					
				// 공격 대상 지역 결정
				BaseLocation targetBaseLocation = InformationManager.Instance().getMainBaseLocation(InformationManager.Instance().enemyPlayer);

				if (targetBaseLocation != null) {
					for (Unit unit : MyBotModule.Broodwar.self().getUnits()) {
						// 건물은 제외
						if (unit.getType().isBuilding())
							continue;
						// 모든 일꾼은 제외
						if (unit.getType().isWorker())
							continue;
											
						// canAttack 유닛은 attackMove Command 로 공격을 보냅니다
						if (unit.canAttack()) {
							if (unit.isIdle()) {
								Unit enemy = getNearestUnit(unit);
								
								if(enemy != null && unit.canAttack(enemy))
									commandUtil.attackMove(unit, enemy.getPoint());
								else
									commandUtil.attackMove(unit, targetBaseLocation.getPoint());
							}
						} 
					}
				}
			}
		}
	}
	
	private void checkAttack() {
		if(MyBotModule.Broodwar.getFrameCount() < 1)
			return;
		
		for(Entry<UnitType, Integer> entry : Config.attackTiming.entrySet()) {
			UnitType key = entry.getKey();
			int count = entry.getValue();			
			int unitCount = InformationManager.Instance().getNumUnits(key, InformationManager.Instance().selfPlayer); 
			
			if(unitCount >= count) {
				System.out.println("Started attack!");
				isFullScaleAttackStarted = true;
				return;
			}
		}
	}
	
	private Unit getNearestUnit(Unit unit) {
		Unit nearestEnemy = null;
		double minDistance = 9999999;

		for(Unit enemy : InformationManager.Instance().enemyPlayer.getUnits()) {
			if(enemy.isInvincible() || enemy.isBurrowed())
				continue;
			
			int distance = unit.getDistance(enemy);
			
			if(distance < minDistance) {
				minDistance = distance;
				nearestEnemy = enemy;
			}
		}

		return nearestEnemy;
	}
	
}