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
public class BuildActionDeck implements Serializable
{
    private static final long serialVersionUID = 77L;
    protected ActionCard ActionList[] = {new ActionCard("After You....", R.drawable.afteryou), new ActionCard("Bribed Guards", R.drawable.bribedguards), new ActionCard("Church Support", R.drawable.churchsupport), new ActionCard("Civic Pride", R.drawable.civicpride), new ActionCard("Civic Support", R.drawable.civicsupport), new ActionCard("Clothing Swap", R.drawable.clothingswap), new ActionCard("Clothing Swap", R.drawable.clothingswap),
        new ActionCard("Confusion in Line", R.drawable.confusioninline), new ActionCard("Double Feature", R.drawable.doublefeature), new ActionCard("Double Feature", R.drawable.doublefeature), new ActionCard("Escape!", R.drawable.escape), new ActionCard("Extra Cart", R.drawable.extracart), new ActionCard("Extra Cart", R.drawable.extracart), new ActionCard("Fainting Spell", R.drawable.faintingspell), new ActionCard("Fainting Spell", R.drawable.faintingspell),
        new ActionCard("Fled to England", R.drawable.fledtoengland), new ActionCard("Forced Break", R.drawable.forcedbreak), new ActionCard("Foreign Support", R.drawable.foreignsupport), new ActionCard("Forward March", R.drawable.forwardmarch), new ActionCard("Fountain of Blood", R.drawable.fountainofblood), new ActionCard("Friend of the Queen", R.drawable.friendofthequeen), new ActionCard("Friend of the Queen", R.drawable.friendofthequeen),
        new ActionCard("Ignoble Noble", R.drawable.ignoblenoble), new ActionCard("Ignoble Noble", R.drawable.ignoblenoble), new ActionCard("Indifferent Public", R.drawable.indifferentpublic), new ActionCard("Information Exchange", R.drawable.informationexchange), new ActionCard("Lack of Faith", R.drawable.lackoffaith), new ActionCard("Late Arrival", R.drawable.latearrival),
        new ActionCard("Let Them Eat Cake", R.drawable.letthemeatcake), new ActionCard("L'Idiot", R.drawable.lidiot), new ActionCard("L'Idiot", R.drawable.lidiot), new ActionCard("Majesty", R.drawable.majesty), new ActionCard("Mass Confusion", R.drawable.massconfusion), new ActionCard("Military Might", R.drawable.militarymight), new ActionCard("Military Support", R.drawable.militarysupport), new ActionCard("Milling in Line", R.drawable.millinginline),
        new ActionCard("Milling in Line", R.drawable.millinginline), new ActionCard("Missed!", R.drawable.missed), new ActionCard("Missed!", R.drawable.missed), new ActionCard("Missing Heads", R.drawable.missingheads), new ActionCard("Missing Heads", R.drawable.missingheads), new ActionCard("Opinionated Guards", R.drawable.opinionatedguards), new ActionCard("Political Influence", R.drawable.politicalinfluence),
        new ActionCard("Political Influence", R.drawable.politicalinfluence), new ActionCard("Public Demand", R.drawable.publicdemand), new ActionCard("Public Demand", R.drawable.publicdemand), new ActionCard("Pushed", R.drawable.pushed), new ActionCard("Pushed", R.drawable.pushed), new ActionCard("Rain Delay", R.drawable.raindelay), new ActionCard("Scarlet Pimpernel", R.drawable.scarletpimpernel), new ActionCard("Stumble", R.drawable.stumble),
        new ActionCard("Stumble", R.drawable.stumble), new ActionCard("The Long Walk", R.drawable.thelongwalk), new ActionCard("The Long Walk", R.drawable.thelongwalk), new ActionCard("'Tis a Far Better Thing", R.drawable.tisafarbetterthing), new ActionCard("Tough Crowd", R.drawable.toughcrowd), new ActionCard("Trip", R.drawable.trip), new ActionCard("Trip", R.drawable.trip), new ActionCard("Was That My Name?", R.drawable.wasthatmyname)};


    public BuildActionDeck()
    {
    }

    public ActionCard[] getActionList() {
        return ActionList;
    }
}
