package edu.up.cs301.guillotine.guillotine;

import java.io.Serializable;

import edu.up.cs301.guillotine.R;

/**
 * This creates the deck of nobles.
 *
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version November 2015
 */
public class BuildNobleDeck implements Serializable
{
    private static final long serialVersionUID = 777L;
    protected   Noble nobleList[] = {new Noble("Tragic Figure","gray", 0, R.drawable.tragicfigure), new Noble("Hero of the People","gray", -3,R.drawable.heroofthepeople), new Noble("The Clown","gray", -2,R.drawable.theclown), new Noble("Innocent Victim","gray", -1,R.drawable.innocentvictim),
            new Noble("Martyr","gray", -1,R.drawable.martyr), new Noble("Martyr","gray", -1,R.drawable.martyr), new Noble("Martyr","gray", -1,R.drawable.martyr), new Noble("Palace Guard","red", 0,R.drawable.palaceguard), new Noble("Palace Guard","red", 0,R.drawable.palaceguard), new Noble("Palace Guard","red", 0,R.drawable.palaceguard), new Noble("Palace Guard","red", 0,R.drawable.palaceguard), new Noble("Palace Guard","red", 0,R.drawable.palaceguard),
            new Noble("Master Spy","red", 4,R.drawable.masterspy), new Noble("General","red", 4,R.drawable.general), new Noble("Colonel","red", 3,R.drawable.colonel), new Noble("Captain of the Guard","red", 2,R.drawable.captainoftheguard), new Noble("Lieutenant","red", 2,R.drawable.lieutenant), new Noble("Lieutenant","red", 2,R.drawable.lieutenant),
            new Noble("Governor","green", 4,R.drawable.governor), new Noble("Mayor","green", 3,R.drawable.mayor), new Noble("Councilman","green", 3,R.drawable.councilman), new Noble("Unpopular Judge","green", 2,R.drawable.unpopularjudge), new Noble("Unpopular Judge","green", 2,R.drawable.unpopularjudge), new Noble("Tax Collector","green", 2,R.drawable.taxcollector),
            new Noble("Land Lord","green", 2,R.drawable.landlord), new Noble("Rival Executioner","green", 1,R.drawable.rivalexecutioner), new Noble("Sheriff","green", 1,R.drawable.sheriff), new Noble("Sheriff","green", 1,R.drawable.sheriff), new Noble("Cardinal","blue", 5,R.drawable.cardinal), new Noble("Archbishop","blue", 4,R.drawable.archbishop), new Noble("Bad Nun","blue", 3,R.drawable.badnun),
            new Noble("Bishop","blue", 2,R.drawable.bishop), new Noble("Heretic","blue", 2,R.drawable.heretic), new Noble("Wealthy Priest","blue", 1,R.drawable.wealthypriest), new Noble("Wealthy Priest","blue", 1,R.drawable.wealthypriest), new Noble("King Louis XVI","purple", 5,R.drawable.kinglouisxvi), new Noble("Marie Antoinette","purple", 5,R.drawable.marieantoinette),
            new Noble("Regent","purple", 4,R.drawable.regent), new Noble("Robespierre","purple", 3,R.drawable.robespierre), new Noble("Duke","purple", 3,R.drawable.duke), new Noble("Baron","purple", 3,R.drawable.baron), new Noble("Lady","purple", 2,R.drawable.lady), new Noble("Lord","purple", 2,R.drawable.lord), new Noble("Countess","purple", 2,R.drawable.countess),
            new Noble("Count","purple", 2,R.drawable.count), new Noble("Fast Noble","purple", 2,R.drawable.fastnoble), new Noble("Lady in Waiting","purple", 1,R.drawable.ladyinwaiting), new Noble("Royal Cartographer","purple", 1,R.drawable.royalcartographer), new Noble("Coiffeur","purple", 1,R.drawable.coiffeur), new Noble("Piss Boy","purple", 1,R.drawable.pissboy)};


    public BuildNobleDeck()
    {
    }

    public Noble[] getNobleList() {
    return nobleList;
}
}
