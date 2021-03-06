package fr.lteconsulting.training.moviedb;

import fr.lteconsulting.training.moviedb.ejb.GestionCategories;
import fr.lteconsulting.training.moviedb.ejb.GestionProduits;
import fr.lteconsulting.training.moviedb.model.Categorie;
import fr.lteconsulting.training.moviedb.model.Produit;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class GestionCategoriesTest {
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Categorie.class.getPackage())
                .addPackage(GestionCategories.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    private GestionCategories gestionCategories;

    @EJB
    private GestionProduits gestionProduits;

    @Test
    public void testAjoutCategorie() {
        Categorie categorie = new Categorie();
        categorie.setNom("test");
        gestionCategories.add(categorie);

        assertNotNull("l'ID ne devrait pas être null", categorie.getId());
        assertEquals("le nom devrait être 'test'", "test", categorie.getNom());
    }

    @Test
    public void testGetNbProduitParCategorieId() {
        Categorie categorie = new Categorie();
        gestionCategories.add(categorie);

        int nb = 10;
        for (int i = 0; i < nb; i++) {
            Produit produit = new Produit();
            produit.setCategorie(categorie);
            gestionProduits.add(produit);
        }

        assertNotNull("l'ID ne devrait pas être null", categorie.getId());
        assertEquals("il devrait y avoir " + nb + " produits pour cette catégorie", nb, gestionCategories.getNbProduitParCategorieId(categorie.getId()));
    }
}
