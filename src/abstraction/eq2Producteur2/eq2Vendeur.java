package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq8Romu.cacaoCriee.IVendeurCacaoCriee;
import abstraction.eq8Romu.cacaoCriee.LotCacaoCriee;
import abstraction.eq8Romu.cacaoCriee.PropositionCriee;
import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Variable;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Pate;
import abstraction.eq8Romu.produits.Gamme;

public class eq2Vendeur extends eq2Stock implements IVendeurCacaoCriee {
	/*Lucas Y
	 *Kristof S
	 */
	private Variable prixTF;
	private Variable prixTT;
	private Variable prixTTE;
	private Variable prixTC;
	private Variable prixTCE;
	private double prixvente;
	private Variable propalsnonvendues;
	private Journal journal_des_ventes;
	private double compteurfora;        //comptent le nombre de fois qu'on a vendu tel type de fève
	private double compteurtrini;
	private double compteurtrinie;
	private double compteurcrio;
	private double compteurcrioe;
	private double prixmaxlot;
	private int compteurinvendus;

	
	public eq2Vendeur() {
		super();
		this.prixTF = new Variable("prixTF",this,1900);
		this.prixTT = new Variable("prixTT",this,2500);
		this.prixTTE = new Variable("prixTTE",this,2800);
		this.prixTC = new Variable("prixTC",this,2900);
		this.prixTCE = new Variable("prixTCE",this,3200);
		this.prixvente = 0;
		this.propalsnonvendues = new Variable("propalsnonvendues",this,999999999);//première valeur super haute pour permettre ventes
		this.journal_des_ventes = new Journal("Journal des ventes", this);
		this.compteurfora = 1.00;
		this.compteurtrini = 1.00;
		this.compteurtrinie = 1.00;
		this.compteurcrio = 1.00;
		this.compteurcrioe = 1.00;
		this.prixmaxlot = 100000;
		this.compteurinvendus = 0;
	}
	/*faudrait rajouter un truc qui set les prix en fonction de ce qu'il s'est passé au cycle d'avant et de notre rentabilité
	 * 
	 */

	/*On vend dès qu'on a du stock
	 * 
	 */
	public LotCacaoCriee getLotEnVente() { 
		List<Variable> Stock = this.getVariablesFeve(); 
	    double masseFora = 0;
	    double masseTrini = 0;
	    double masseTriniE = 0;
	    double masseCrio = 0;
	    double masseCrioE = 0;
	    double prixfora = 0;
	    double prixtrini = 0;
	    double prixtrinie = 0;
	    double prixcrio = 0;
	    double prixcrioe = 0;
	    
	    for (int i = 0; i < Stock.size();i++) { //changer ça vu que c'est plus une liste de couples mais un dictionnaire (en fait y'a peut-être pas besoin de le changer)
	    	if (Stock.get(i).getNom() == "EQ2Feve.FEVE_BASSE") {
	    		masseFora = masseFora + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "EQ2Feve.FEVE_MOYENNE") {
	    		masseTrini = masseTrini + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "EQ2Feve.FEVE_MOYENNE_EQUITABLE") {
	    		masseTriniE = masseTriniE + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "EQ2Feve.FEVE_HAUTE") {
	    		masseCrio = masseCrio + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "EQ2Feve.FEVE_HAUTE_EQUITABLE") {
	    		masseCrioE = masseCrioE + Stock.get(i).getValeur();
	    	}
	    }
	    //this.journal_des_ventes.ajouter("masseFora="+masseFora+"  masseTrini="+masseTrini+"  masseTriniE"+masseTriniE+"  masseCrio="+masseCrio+"  masseCrioE="+masseCrioE); //pour débugger

	    prixfora = masseFora*this.getPrixTF().getValeur();
	    prixtrini = masseTrini*this.getPrixTT().getValeur();
	    prixtrinie = masseTriniE*this.getPrixTTE().getValeur();
	    prixcrio = masseCrio*this.getPrixTC().getValeur();
	    prixcrioe = masseCrioE*this.getPrixTCE().getValeur();
	    //this.journal_des_ventes.ajouter("prixfora="+prixfora+"  prixTrini="+prixtrini+"  prixTriniE"+prixtrinie+"  prixCrio="+prixcrio+"  prixCrioE="+prixcrioe); //pour débugger

	    if (prixfora > prixtrini && prixfora > prixcrio && masseFora > 0.5 && prixfora > prixtrinie && prixfora > prixcrioe) {
	    	if (this.getPropal()>prixfora) {
	    		if (prixfora > this.getprixmaxlot()) {
	    			this.setPrixVente(this.getprixmaxlot());
	    			this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de forastero de " + this.getprixmaxlot()/this.getPrixTF().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    			return new LotCacaoCriee(this, Feve.FEVE_BASSE, this.getprixmaxlot()/this.getPrixTF().getValeur(), this.getPrixTF().getValeur());
	    		}
	    	}
	    	else if ((this.getPropal())/this.getPrixTF().getValeur()>0.5){
	    		this.setPrixVente(this.getPropal());
	    		this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de forastero de " + (this.getPropal())/this.getPrixTF().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_BASSE, (this.getPropal())/this.getPrixTF().getValeur(), this.getPrixTF().getValeur());
	    	}
	    		
	    }
	    else if (prixtrini > prixfora && prixtrini > prixcrio && masseTrini > 0.5 && prixtrini > prixtrinie && prixtrini > prixcrioe) {
	    	if (this.getPropal()>prixtrini) {
	    		if (prixtrini > this.getprixmaxlot()) {
	    			this.setPrixVente(this.getprixmaxlot());
	    			this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de trinitario de " + this.getprixmaxlot()/this.getPrixTT().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    			return new LotCacaoCriee(this, Feve.FEVE_MOYENNE, this.getprixmaxlot()/this.getPrixTT().getValeur(), this.getPrixTT().getValeur());
	    		}
	    	}
	    	else if ((this.getPropal())/this.getPrixTT().getValeur()>0.5){
	    		this.setPrixVente(this.getPropal());
	    		this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de trinitario de " + (this.getPropal())/this.getPrixTT().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_MOYENNE, (this.getPropal())/this.getPrixTT().getValeur(), this.getPrixTT().getValeur());
	    	}
	    		
	    }
	    else if (prixtrinie > prixfora && prixtrinie > prixcrio && masseTriniE > 0.5 && prixtrinie > prixtrini && prixtrinie > prixcrioe) {
	    	if (this.getPropal()>prixtrinie) {
	    		if (prixtrinie > this.getprixmaxlot()) {
	    			this.setPrixVente(this.getprixmaxlot());
	    			this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de trinitario de équitable " + this.getprixmaxlot()/this.getPrixTTE().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    			return new LotCacaoCriee(this, Feve.FEVE_MOYENNE_EQUITABLE, this.getprixmaxlot()/this.getPrixTTE().getValeur(), this.getPrixTTE().getValeur());
	    		}
	    	}
	    	else if ((this.getPropal())/this.getPrixTTE().getValeur()>0.5){
	    		this.setPrixVente(this.getPropal());
	    		this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de trinitario équitable de " + (this.getPropal())/this.getPrixTTE().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_MOYENNE_EQUITABLE, (this.getPropal())/this.getPrixTTE().getValeur(), this.getPrixTTE().getValeur());
	    	}
	    		
	    }
	    else if (prixcrio > prixtrini && prixfora < prixcrio && masseCrio > 0.5 && prixcrio > prixtrinie && prixcrio > prixcrioe) {
	    	if (this.getPropal()>prixcrio) {
	    		if (prixcrio > this.getprixmaxlot()) {
	    			this.setPrixVente(this.getprixmaxlot());
	    			this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de criollo de " + this.getprixmaxlot()/this.getPrixTC().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    			return new LotCacaoCriee(this, Feve.FEVE_HAUTE, this.getprixmaxlot()/this.getPrixTC().getValeur(), this.getPrixTC().getValeur());
	    		}
	    	}
	    	else if((this.getPropal())/this.getPrixTC().getValeur()>0.5){
	    		this.setPrixVente(this.getPropal());
	    		this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de criollo de " + (this.getPropal())/this.getPrixTC().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_HAUTE, (this.getPropal())/this.getPrixTC().getValeur(), this.getPrixTC().getValeur());
	    	}
	    		
	    }
	    else if (prixcrioe > prixtrini && prixfora < prixcrioe && masseCrioE > 0.5 && prixcrioe > prixtrinie && prixcrioe > prixcrio) {
	    	if (this.getPropal()>prixcrioe) {
	    		if (prixcrioe > this.getprixmaxlot()) {
	    			this.setPrixVente(this.getprixmaxlot());
	    			this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de criollo équitable de " + this.getprixmaxlot()/this.getPrixTCE().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    			return new LotCacaoCriee(this, Feve.FEVE_HAUTE_EQUITABLE, this.getprixmaxlot()/this.getPrixTCE().getValeur(), this.getPrixTCE().getValeur());
	    		}
	    	}
	    	else if((this.getPropal())/this.getPrixTCE().getValeur()>0.5){
	    		this.setPrixVente(this.getPropal());
	    		this.journal_des_ventes.ajouter("EQ2 : Proposition de vente d'un lot de criollo équitable de " + (this.getPropal())/this.getPrixTCE().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_HAUTE_EQUITABLE, (this.getPropal())/this.getPrixTCE().getValeur(), this.getPrixTCE().getValeur());
	    	}
	    		
	    }
	    
	    this.journal_des_ventes.ajouter("Aucun lot n'a été proposé à la vente");
	    this.setcompteurinvendus(this.getcompteurinvendus()+10);
	    return null;
	    
		
	}
	

	@Override
	public void notifierAucuneProposition(LotCacaoCriee lot) { 
		this.setPropal(this.getPrixVente()*0.95);
		this.journal_des_ventes.ajouter("EQ2 : Aucune proposition de lot n'a abouti à une vente");
		this.setcompteurinvendus(this.getcompteurinvendus()+1);
		this.journal_des_ventes.ajouter("compteur des invendus = "+this.getcompteurinvendus());
		
	}

	@Override
	public PropositionCriee choisir(List<PropositionCriee> propositions) { // récupère l'offre d'achat la plus élevée et l'accepte si écart prix <= 5%
		int indice = 0;
		double max_actuel = 0;
		for (int i = 0; i < propositions.size(); i++) {
			if (max_actuel < propositions.get(i).getPrixPourLeLot()) {
				max_actuel = propositions.get(i).getPrixPourLeLot();
				indice = i;
			} //rajouter prix de vente du lot au journal pour être sur
		}
		if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_BASSE) { //changer le abs pour qu'ils puissent nous l'acheter plus cher
			if ((propositions.get(indice).getPrixPourUneTonne() > this.getPrixTF().getValeur()) || (Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTF().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("EQ2 accepte la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_MOYENNE) {
			if (propositions.get(indice).getPrixPourUneTonne() > this.getPrixTT().getValeur() || (Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTT().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("EQ2 accepte la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_MOYENNE_EQUITABLE) {
			if (propositions.get(indice).getPrixPourUneTonne() > this.getPrixTTE().getValeur() || (Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTTE().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("EQ2 accepte la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_HAUTE) {
			if (propositions.get(indice).getPrixPourUneTonne() > this.getPrixTC().getValeur() || (Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTC().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("EQ2 accepte la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_HAUTE_EQUITABLE) {
			if (propositions.get(indice).getPrixPourUneTonne() > this.getPrixTCE().getValeur() || (Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTCE().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("EQ2 accepte la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		return null;
	}

	@Override
	public void notifierVente(PropositionCriee proposition) { //enlève fèves vendues de notre stock
		removeQtFeve(proposition.getLot().getFeve(), proposition.getLot().getQuantiteEnTonnes());
		this.journal_des_ventes.ajouter("EQ2 : La vente a aboutit, le stock a été actualisé STONKS");
		this.setcompteurinvendus(0);
		this.journal_des_ventes.ajouter("le compteur des invendus vaut" + this.getcompteurinvendus());
		Feve feve = proposition.getFeve();
		double prixtonne = proposition.getPrixPourUneTonne();
		if (feve==Feve.FEVE_BASSE) {
			compteurfora ++;
			/*if (prixtonne > this.getPrixTF().getValeur()) {
				this.prixTF.setValeur(this, this.getPrixTF().getValeur() + 0.7*(prixtonne - this.getPrixTF().getValeur()));
			}*/
		}
		else if (feve==Feve.FEVE_MOYENNE) {
			compteurtrini ++;
			/*if (prixtonne > this.getPrixTT().getValeur()) {
				this.prixTT.setValeur(this, this.getPrixTT().getValeur() + 0.7*(prixtonne - this.getPrixTT().getValeur()));
			}*/
		}
		else if (feve==Feve.FEVE_MOYENNE_EQUITABLE) {
			compteurtrinie ++;
			/*if (prixtonne > this.getPrixTTE().getValeur()) {
				this.prixTTE.setValeur(this, this.getPrixTTE().getValeur() + 0.7*(prixtonne - this.getPrixTTE().getValeur()));
			}*/
		}
		else if (feve==Feve.FEVE_HAUTE) {
			compteurcrio ++;
			/*if (prixtonne > this.getPrixTC().getValeur()) {
				this.prixTC.setValeur(this, this.getPrixTC().getValeur() + 0.7*(prixtonne - this.getPrixTC().getValeur()));
			}*/
		}
		else if (feve==Feve.FEVE_HAUTE_EQUITABLE) {
			compteurcrioe ++;
			/*if (prixtonne > this.getPrixTCE().getValeur()) {
				this.prixTCE.setValeur(this, this.getPrixTCE().getValeur() + 0.7*(prixtonne - this.getPrixTCE().getValeur()));
			}*/
		}
	}
	
	public double getPrixVente() {
		return this.prixvente;
	}
	
	public void setPrixVente(double prix) {
		this.prixvente = prix;
	}
	
	public double getPropal() {
		return this.propalsnonvendues.getValeur();
	}
	
	public void setPropal(double propal) {
		this.propalsnonvendues.setValeur(this, propal);
	}
	
	/**
	 * @return the prixTF
	 */
	public Variable getPrixTF() {
		return prixTF;
	}

	/**
	 * @param prixTF the prixTF to set
	 */
	public void setPrixTF(Variable prixTF) {
		this.prixTF = prixTF;
	}

	/**
	 * @return the prixTT
	 */
	public Variable getPrixTT() {
		return prixTT;
	}

	/**
	 * @param prixTT the prixTT to set
	 */
	public void setPrixTT(Variable prixTT) {
		this.prixTT = prixTT;
	}

	/**
	 * @return the prixTC
	 */
	public Variable getPrixTC() {
		return prixTC;
	}

	/**
	 * @param prixTC the prixTC to set
	 */
	public void setPrixTC(Variable prixTC) {
		this.prixTC = prixTC;
	}

	/**
	 * @return the prixTTE
	 */
	public Variable getPrixTTE() {
		return prixTTE;
	}

	/**
	 * @param prixTTE the prixTTE to set
	 */
	public void setPrixTTE(Variable prixTTE) {
		this.prixTTE = prixTTE;
	}

	/**
	 * @return the prixTCE
	 */
	public Variable getPrixTCE() {
		return prixTCE;
	}

	/**
	 * @param prixTCE the prixTCE to set
	 */
	public void setPrixTCE(Variable prixTCE) {
		this.prixTCE = prixTCE;
	}
	
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.addAll(super.getJournaux());
		res.add(this.journal_des_ventes);
		return res;
	}
	
	public double getcompteurfora() {
		return this.compteurfora;
	}
	public double getcompteurtrini() {
		return this.compteurtrini;
	}
	public double getcompteurtrinie() {
		return this.compteurtrinie;
	}
	public double getcompteurcrio() {
		return this.compteurcrio;
	}
	public double getcompteurcrioe() {
		return this.compteurcrioe;
	}
	public double getprixmaxlot() {
		return this.prixmaxlot;
	}
	public void setcompteurinvendus(int i) {
		this.compteurinvendus = i;
	}
	public int getcompteurinvendus() {
		return this.compteurinvendus;
	}
}
	


