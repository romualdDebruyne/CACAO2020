package abstraction.eq6Distributeur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abstraction.eq8Romu.cacaoCriee.FiliereTestVentesCacaoCriee;
import abstraction.eq8Romu.chocolatBourse.FiliereTestVentesChocolatBourse;
import abstraction.eq8Romu.clients.ClientFinal;
import abstraction.eq8Romu.clients.FiliereTestClientFinal;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.ventesCacaoAleatoires.FiliereVentesCacaoAleatoires;
import abstraction.fourni.Filiere;
import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Variable;

public class Distributeur1abs implements IActeur {

	protected Integer cryptogramme;
	protected Journal journalEq6, journalEq6Stock;
	protected Variable stockHGE;
	protected Variable stockMG;
	protected Variable stockBG;
	protected Map<Integer,Map<Chocolat,Double>> evolutionCours;
	protected Map<ChocolatDeMarque,Double> MapStock;
	protected Map<Integer,Map<ChocolatDeMarque,Double>> evolutionVentes;

	public Distributeur1abs() { 
		this.stockHGE=new Variable(getNom()+" stock "+ Chocolat.CHOCOLAT_HAUTE_EQUITABLE.toString(), this, 0, 1000000000, 1000000);
		this.stockMG=new Variable(getNom()+"stock"+ Chocolat.CHOCOLAT_MOYENNE.toString(), this, 0, 1000000000, 1000000);
		this.stockBG=new Variable(getNom()+"stock"+ Chocolat.CHOCOLAT_BASSE.toString(), this, 0, 1000000000, 1000000);
		this.journalEq6=new Journal(this.getNom()+" activites", this);
		this.journalEq6Stock=new Journal(this.getNom()+" stock", this);
		evolutionCours = new HashMap<Integer,Map<Chocolat,Double>>();
		this.MapStock = new HashMap<ChocolatDeMarque,Double>();
		this.evolutionVentes = new HashMap<Integer, Map<ChocolatDeMarque,Double>>();
		this.evolutionVentes.put(0,new HashMap<ChocolatDeMarque,Double>());
	}

	public String getNom() {
		return "IMTermarché";
	}

	public String getDescription() {
		return "Distributeur bla bla bla";
	}

	public Color getColor() {
		return new Color(230, 126, 34);
	}
	
	public void stocker(ChocolatDeMarque choco, double quantite) {
		
	}
	public void initialiser() {
		for (ChocolatDeMarque choco : ClientFinal.tousLesChocolatsDeMarquePossibles()) {
			if (choco.getChocolat()==Chocolat.CHOCOLAT_HAUTE_EQUITABLE) {
				journalEq6Stock.ajouter("2345");
				stocker(choco, 2345);
			}
				
		}
	}

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	public double quantiteEnStockTypeChoco(Chocolat choco) {
		return 0;
	}
	
	/** @author Luca Pinguet & Mélissa Tamine */
	public void next() {
		journalEq6.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		this.evolutionCours.put(Filiere.LA_FILIERE.getEtape(),new HashMap<Chocolat,Double>());
		this.evolutionVentes.put(Filiere.LA_FILIERE.getEtape()+1,new HashMap<ChocolatDeMarque,Double>());
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			for (ChocolatDeMarque chocos : this.MapStock.keySet()) {
				journalEq6.ajouter("Le prix moyen du chocolat \""+chocos.name()+"\" a l'etape precedente etait de "+Filiere.LA_FILIERE.prixMoyenEtapePrecedente(chocos));

			}
			for (ChocolatDeMarque chocos : this.MapStock.keySet()) {
				journalEq6.ajouter("Les ventes de chocolat \""+chocos.name()+" il y a un an etaient de "+Filiere.LA_FILIERE.getVentes(Filiere.LA_FILIERE.getEtape()-24, chocos));

			}
		}
		double somme = 0;
		journalEq6Stock.ajouter("" + this.MapStock.keySet().size());
		for (ChocolatDeMarque chocos : this.MapStock.keySet()) {
			somme= somme+ this.MapStock.get(chocos);
			journalEq6Stock.ajouter("La quantite de chocolat en stock est"+somme);
		}
		this.stockMG.setValeur(this, quantiteEnStockTypeChoco(Chocolat.CHOCOLAT_MOYENNE));
		this.stockBG.setValeur(this, quantiteEnStockTypeChoco(Chocolat.CHOCOLAT_BASSE));
		this.stockHGE.setValeur(this, quantiteEnStockTypeChoco(Chocolat.CHOCOLAT_HAUTE_EQUITABLE));
		
	}

	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		filieres.add("AchatBourseSeul"); //Simulation achat bourse sans concurrence
		filieres.add("AchatBourseConcu"); //Simulation achat bourse avec concurrence
		filieres.add("VenteSeul"); //Simulation vente sans concurrence
		filieres.add("VenteConcu"); //Simulation vente avec concurrence
		return filieres;
	}

	public Filiere getFiliere(String nom) {
		switch (nom) {
		case "AchatBourseSeul" : return new FiliereAchatSeul();
		case "AchatBourseConcu" : return new FiliereAchatConcu();
		case "VenteSeul" : return new FiliereVenteSeul();
		case "VenteConcu" : return new FiliereVenteConcu();
		default : return null;}
	}

	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(stockHGE);
		res.add(stockBG);
		res.add(stockMG);

		return res;
	}

	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journalEq6);
		res.add(this.journalEq6Stock);
		return res;
	}

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
			System.out.println("I'll be back... or not... "+this.getNom());
		} else {
			System.out.println("Poor "+acteur.getNom()+"... We will miss you. "+this.getNom());
		}
	}

	public void notificationOperationBancaire(double montant) {
	}
}

