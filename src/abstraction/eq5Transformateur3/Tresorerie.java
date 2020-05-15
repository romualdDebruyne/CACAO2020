package abstraction.eq5Transformateur3;

import abstraction.fourni.Banque;
import abstraction.fourni.Filiere;
import abstraction.fourni.Variable;

/** @author Simon MORO  
     * Gère l'argent de la société pour la répartir entre les filiaires et voir si toutes les décisions sont cohérentes en terme bugetaires.
     * Elle est agrégé dans la classe Transformateur3.
     * 
     * Infos utiles à récupérer : montant compte en banque ; stock ; historique des ventes / achats ; agios / découverts 
     * ; montant max d'investissement de chaque filière ;
     * 
     *structure d'une Variable = (String nom, IActeur createur,  double min, double max, double valInit)
     */
public class Tresorerie {
    
	private Transformateur3 acteur;
	//private double MontantCompte;				//Montant réel du compte au début du tour
	private double MontantCompteALaFinDuTour;	//Montant théorique après les achats de quelqu'un
	//private double Decouvert;
	private double FacteurPrioriteGamme; // 100% haute gamme = 1, 100% bas de gamme = 0

	
	//private Variable decouvertsConsecutifsAvantFaillite; //parametres fixes à priori
	//private Variable decouvertAutorise;
	//private Variable agiosDecouvertAutorise;
	//private Variable agiosDecouvertAuDela;
	//private Variable seuilOperationsRefusees;
	
	/**public Tresorerie(Transformateur3 acteur, double MontantCompte, double MontantCompteALaFinDuTour, double Decouvert, double Facteur, Variable decouvertsConsecutifsAvantFaillite,
			Variable decouvertAutorise, Variable agiosDecouvertAutorise, Variable agiosDecouvertAuDela, Variable seuilOperationsRefusees) {
		this.acteur = acteur;
		this.MontantCompte=MontantCompte;
		this.MontantCompteALaFinDuTour=MontantCompteALaFinDuTour;
		this.Decouvert=Decouvert;
		this.FacteurPrioriteGamme=Facteur;
		
		this.decouvertsConsecutifsAvantFaillite = decouvertsConsecutifsAvantFaillite;
		this.decouvertAutorise = decouvertAutorise;
		this.agiosDecouvertAutorise = agiosDecouvertAutorise;
		this.agiosDecouvertAuDela = agiosDecouvertAuDela;
		this.seuilOperationsRefusees = seuilOperationsRefusees;
	}
	/**
	     * Initialise la trésorerie
	     */
	//public Tresorerie(Transformateur3 acteur) {
		/**
		 * La trésorerie est initialisée comme vide, elle est mise à jour en début de tour idéalement
		 */
		/**this(acteur,
				0,									//montantCompte
				0,									//MontantCompteALaFinDuTour
				0,									//decouvert actuel
				0,									//FacteurPriorite
				Filiere.LA_FILIERE.getBanque().getParametres().get(0),
				Filiere.LA_FILIERE.getBanque().getParametres().get(1),
				Filiere.LA_FILIERE.getBanque().getParametres().get(2),
				Filiere.LA_FILIERE.getBanque().getParametres().get(3),
				Filiere.LA_FILIERE.getBanque().getParametres().get(4));
	}*/
	
	public Tresorerie(Transformateur3 acteur, double MontantCompteALaFinDuTour, double Facteur) {
		this.acteur = acteur;
		this.MontantCompteALaFinDuTour=MontantCompteALaFinDuTour;
		this.FacteurPrioriteGamme=Facteur;
	}

	public Tresorerie(Transformateur3 acteur) {
		this(acteur,
				0,
				0.8);
	}
	
	
	public Transformateur3 getActeur() {
		return acteur;
	}

	public double getMontantCompte() {
		return Filiere.LA_FILIERE.getBanque().getSolde(this.acteur, this.acteur.getCryptogramme());
	}
	
	public double getMontantCompteALaFinDuTour() {
		return this.MontantCompteALaFinDuTour;
	}
	
	public double getDecouvert() {
		return 0; //jsp
	}
	/**
	     * Le facteur qui décrit la priorité que nous mettons sur le bas de gamme par rapport au haut de gamme (évolue au cours de la
	     * simulation)
	     */
	public double getFacteurPrioriteGamme() {
		
		return this.FacteurPrioriteGamme;
	}
		
	public Variable getDecouvertsConsecutifsAvantFaillite() {
		return Filiere.LA_FILIERE.getBanque().getParametres().get(0);
	}
	
	public Variable getDecouvertAutorise() {
		return Filiere.LA_FILIERE.getBanque().getParametres().get(1);
	}
	
	public Variable getAgiosDecouvertAutorise() {
		return Filiere.LA_FILIERE.getBanque().getParametres().get(2);
	}
	
	public Variable getAgiosDecouvertAuDela() {
		return Filiere.LA_FILIERE.getBanque().getParametres().get(3);
	}
	
	public Variable getSeuilOperationsRefusees() {
		return Filiere.LA_FILIERE.getBanque().getParametres().get(4);
	}
	
	public void setMontantCompteALaFinDuTour (double MontantCompteALaFinDuTour) {
		this.MontantCompteALaFinDuTour=MontantCompteALaFinDuTour;
	}

	/**
	     * Le facteur qui décrit la priorité que nous mettons sur le bas de gamme par rapport au haut de gamme (évolue au cours de la
	     * simulation)
	     */
	public void setFacteurPrioriteGamme (double Facteur) {
		
		this.FacteurPrioriteGamme=Facteur;
	}
	/**
	     * Renvoie l'investissement maximum possible à faire dans le bas de gamme en fonction du facteur de priorité que l'on s'impose
	     * ainsi que du montant de notre trésorerie
	     */
	
	public void next() {
		/**
		 * Met à jour toute la trésorerie
		 */
		this.setFacteurPrioriteGamme(this.getFacteurPrioriteGamme()); //pour l'instant constant
		this.setMontantCompteALaFinDuTour(0); 						//réinitialise la valeur temporaire
	}
	
	public double investissementMaxBasDeGamme() {
		
		return this.getMontantCompte();
	}
	
	/**
	     * Renvoie l'investissement maximum possible à faire dans le haut de gamme en fonction du facteur de priorité que l'on s'impose
	     * ainsi que du montant de notre trésorerie
	     */
	public double investissementMaxHautDeGamme() {
		
		return this.getMontantCompte();
	}
	
	public void diminueTresorerie(double montant) {
		Filiere.LA_FILIERE.getBanque().virer(this.getActeur(), this.getActeur().getCryptogramme(), Filiere.LA_FILIERE.getBanque(), montant);
	}
	
}