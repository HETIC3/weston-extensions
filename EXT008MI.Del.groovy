/**
 * README
 * Type : ExtendM3Transaction
 * Name: EXT008MI.Del
 * Description: Del a record in EXT008
 * Date                         Changed By                    Description
 * 20250604                     ddecosterd@hetic3.fr     		création
 */
public class DEL extends ExtendM3Transaction {
	private final MIAPI mi;
	private final ProgramAPI program;
	private final DatabaseAPI database;
	private final UtilityAPI utility;
	
	/*
	 * Transaction EXT008MI/DEL Interface
	 * @param mi - Infor MI Interface
	 * @param program - Infor Program API
	 * @param database - Infor Database Interface
	 * @param utility - Utility
	 */
	public DEL(MIAPI mi, ProgramAPI program, DatabaseAPI database, UtilityAPI utility) {
		this.mi = mi;
		this.program = program;
		this.database = database;
		this.utility = utility;
	}

	public void main() {
		Integer cono = mi.in.get("CONO");
		String  faci = (mi.inData.get("FACI") == null) ? "" : mi.inData.get("FACI").trim();
		Long mere = mi.in.get("MERE");
		String  plgr = (mi.inData.get("PLGR") == null) ? "" : mi.inData.get("PLGR").trim();

		if(cono == null) {
			mi.error("La division est obligatoire.");
			return;
		}

		if(faci.isBlank()) {
			mi.error("L'établissement est obligatoire.");
			return;
		}


		if(mere == null) {
			mi.error("La note mère est obligatoire");
			return;
		}

		if(plgr.isBlank()) {
			mi.error("Poste de charge est obligatoire.");
			return;
		}
		
		DBAction ext008Record = database.table("EXT008").index("00").build();
		DBContainer ext008Container = ext008Record.createContainer();
		ext008Container.setInt("EXCONO", cono);
		ext008Container.setString("EXFACI", faci);
		ext008Container.setLong("EXMERE", mere);
		ext008Container.setString("EXPLGR", plgr);

		boolean deleted = ext008Record.readLock(ext008Container, { LockedResult delRecoord ->
			delRecoord.delete();
		});

		if(!deleted)
		{
			mi.error("L'enregistrement n'existe pas.");
			return;
		}

	}

}