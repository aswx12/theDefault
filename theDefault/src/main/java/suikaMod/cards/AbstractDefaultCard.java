package suikaMod.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractDefaultCard extends CustomCard
{
    private static final Logger logger = LogManager.getLogger(AbstractCard.class.getName());
    // Custom Abstract Cards can be a bit confusing. While this is a simple base for simply adding a second magic number,
    // if you're new to modding I suggest you skip this file until you know what unique things that aren't provided
    // by default, that you need in your own cards.

    // In this example, we use a custom Abstract Card in order to define a new magic number. From here on out, we can
    // simply use that in our cards, so long as we put "extends AbstractDynamicCard" instead of "extends CustomCard" at the start.
    // In simple terms, it's for things that we don't want to define again and again in every single card we make.

    //region dmg

    public int dmgPerEnergy; //DPE
    public int baseDmgPerEnergy;

    public int multiDPE[];

    public boolean upgradedDPE;
    public boolean isDPEModified;

    public int vampDmg;
    public int baseVampDmg;

    public int multiVampDmg[];

    public boolean upgradedVampDmg;
    public boolean isVampDmgModified;

    public int dmgPsnCondition;
    public int baseDmgPsnCondition;

    public boolean upgradedDmgPsnCondition;
    public boolean isDmgPsnConditionModified;

    public int dmgPerAttPlayed; //DPAP
    public int baseDPAP;

    public boolean upgradedDPAP;
    public boolean isDPAPModified;

    public int dmgPerSkillInHand; //DPSH
    public int baseDPSH;

    public boolean upgradedDPSH;
    public boolean isDPSHModified;

    int DMGArray[] = {
            this.damage,
            this.dmgPerEnergy,
            this.vampDmg,
            this.dmgPsnCondition,
            this.dmgPerAttPlayed,
            this.dmgPerSkillInHand,
    };

    boolean dmgBoolArray[] = new boolean[6];

    int baseDmgArray[] = {
            this.baseDamage,
            this.baseDmgPerEnergy,
            this.baseVampDmg,
            this.baseDmgPsnCondition,
            this.baseDPAP,
            this.baseDPSH,
    };
    //endregion


    //region vulnerable
    public int aVulnerableValue;        // Just like magic number, or any number for that matter, we want our regular, modifiable stat
    public int aBaseVulnerableValue;    // And our base stat - the number in it's base state. It will reset to that by default.
    public boolean aUpgradedVulnerableValue; // A boolean to check whether the number has been upgraded or not.
    public boolean aIsVulnerableValueModified; // A boolean to check whether the number has been modified or not, for coloring purposes. (red/green)

    public int gVulnerableValue;
    public int gBaseVulnerableValue;
    public boolean gUpgradedVulnerableValue;
    public boolean gIsVulnerableValueModified;
    //endregion

    //region weak
    public int aWeakValue;
    public int aBaseWeakValue;
    public boolean aUpgradedWeakValue;
    public boolean aIsWeakValueModified;

    public int gWeakValue;
    public int gBaseWeakValue;
    public boolean gUpgradedWeakValue;
    public boolean gIsWeakValueModified;
    //endregion

    //region poison
    public int aPoisonValue;
    public int aBasePoisonValue;
    public boolean aUpgradedPoisonValue;
    public boolean aIsPoisonValueModified;

    public int gPoisonValue;
    public int gBasePoisonValue;
    public boolean gUpgradedPoisonValue;
    public boolean gIsPoisonValueModified;
    //endregion

    //region str
    public int aStrValue;
    public int aBaseStrValue;
    public boolean aUpgradedStrValue;
    public boolean aIsStrValueModified;

    public int gStrValue;
    public int gBaseStrValue;
    public boolean gUpgradedStrValue;
    public boolean gIsStrValueModified;



    public AbstractDefaultCard(final String id,
                               final String name,
                               final String img,
                               final int cost,
                               final String rawDescription,
                               final CardType type,
                               final CardColor color,
                               final CardRarity rarity,
                               final CardTarget target)
    {

        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;


      /*  isDamageModified = false;

        isDPEModified = false;
        isVampDmgModified = false;
        isDmgPsnConditionModified = false;
        isDPAPModified = false;
        isDPSHModified = false;*/
        DmgModifySetFalse();

        isBlockModified = false;

        isMagicNumberModified = false;

        aIsVulnerableValueModified = false;
        aIsWeakValueModified = false;
        aIsPoisonValueModified = false;
        aIsStrValueModified = false;

        gIsVulnerableValueModified = false;
        gIsWeakValueModified = false;
        gIsPoisonValueModified = false;
        gIsStrValueModified = false;

    }

    public void displayUpgrades()
    { // Display the upgrade - when you click a card to upgrade it
        super.displayUpgrades();

        if (upgradedDPE)
        {
            dmgPerEnergy = baseDmgPerEnergy;
            isDPEModified = true;
        }

        if (upgradedVampDmg)
        {
            vampDmg = baseVampDmg;
            isVampDmgModified = true;
        }

        if (upgradedDmgPsnCondition)
        {
            baseDmgPsnCondition = dmgPsnCondition;
            isDmgPsnConditionModified = true;
        }

        if (upgradedDPAP)
        {
            baseDPAP = dmgPerAttPlayed;
            isDPAPModified = true;
        }
        if (upgradedDPSH)
        {
            baseDPSH = dmgPerSkillInHand;
            isDPSHModified = true;
        }

        if (aUpgradedVulnerableValue)
        { // If we set upgradedDefaultSecondMagicNumber = true in our card.
            aVulnerableValue = aBaseVulnerableValue; // Show how the number changes, as out of combat, the base number of a card is shown.
            aIsVulnerableValueModified = true; // Modified = true, color it green to highlight that the number is being changed.
        }

        //region buff/debuff
        if (aUpgradedWeakValue)
        {
            aWeakValue = aBaseWeakValue;
            aIsWeakValueModified = true;
        }

        if (aUpgradedPoisonValue)
        {
            aPoisonValue = aBasePoisonValue;
            aIsPoisonValueModified = true;
        }

        if (aUpgradedStrValue)
        {
            aStrValue = aBaseStrValue;
            aIsStrValueModified = true;
        }

        if (gUpgradedVulnerableValue)
        {
            gVulnerableValue = gBaseVulnerableValue;
            gIsVulnerableValueModified = true;
        }

        if (gUpgradedWeakValue)
        {
            gWeakValue = gBaseWeakValue;
            gIsWeakValueModified = true;
        }

        if (gUpgradedPoisonValue)
        {
            gPoisonValue = gBasePoisonValue;
            gIsPoisonValueModified = true;
        }

        if (gUpgradedStrValue)
        {
            gStrValue = gBaseStrValue;
            gIsStrValueModified = true;
        }
        //endregion
    }

    //region Upgrade
    @Override
    protected void upgradeDamage(int amount)
    {
        this.baseDamage = amount;
        this.upgradedDamage = true;
    }

    protected void upgradeDPE(int amount)
    {
        this.baseDmgPerEnergy = amount;
        this.upgradedDPE = true;
    }

    protected void upgradeVampDmg(int amount)
    {
        this.baseVampDmg = amount;
        this.upgradedVampDmg = true;
    }

    protected void upgradeDmgPsnCon(int amount)
    {
        this.baseDmgPsnCondition = amount;
        this.upgradedDmgPsnCondition = true;
    }

    protected void upgradeDPAP(int amount)
    {
        this.baseDPAP = amount;
        this.upgradedDPAP = true;
    }

    protected void upgradeDPSH(int amount)
    {
        this.baseDPSH = amount;
        this.upgradedDPSH = true;
    }

    @Override
    protected void upgradeBlock(int amount)
    {
        this.baseBlock = amount;
        this.upgradedBlock = true;
    }

    @Override
    protected void upgradeMagicNumber(int amount)
    {
        this.baseMagicNumber = amount;
        this.magicNumber = this.baseMagicNumber;
        this.upgradedMagicNumber = true;
    }

    //region Apply
    public int upgradeAVulnerableValue(int amount)
    { // If we're upgrading (read: changing) the number. Note "upgrade" and NOT "upgraded" - 2 different things. One is a boolean, and then this one is what you will usually use - change the integer by how much you want to upgrade.
        aBaseVulnerableValue = amount; // Upgrade the number by the amount you provide in your card.
        aVulnerableValue = aBaseVulnerableValue; // Set the number to be equal to the base value.
        aUpgradedVulnerableValue = true; // Upgraded = true - which does what the above method does.
        return aVulnerableValue;
    }

/*    public void upgradeAValues(int amount, int baseValue, int value, boolean upgradeBool)
    {
        baseValue = amount;
        value = baseValue;
        upgradeBool = true;
    }*/

    public int upgradeAWeakValue(int amount)
    {
        aBaseWeakValue = amount;
        aWeakValue = aBaseWeakValue;
        aUpgradedWeakValue = true;
        return aWeakValue;
    }

    public int upgradeAPoisonValue(int amount)
    {
        aBasePoisonValue = amount;
        aPoisonValue = aBasePoisonValue;
        aUpgradedPoisonValue = true;
        return aPoisonValue;
    }

    public int upgradeAStrValue(int amount)
    {
        aBaseStrValue = amount;
        aStrValue = aBaseStrValue;
        aUpgradedStrValue = true;
        return aStrValue;
    }
    //endregion

    //region Gain

    public int upgradeGVulnerableValue(int amount)
    {
        gBaseVulnerableValue = amount;
        gVulnerableValue = gBaseVulnerableValue;
        gUpgradedVulnerableValue = true;

        return gVulnerableValue;
    }

    public int upgradeGWeakValue(int amount)
    {
        gBaseWeakValue = amount;
        gWeakValue = gBaseWeakValue;
        gUpgradedWeakValue = true;
        return gWeakValue;
    }

    public int upgradeGPoisonValue(int amount)
    {
        gBasePoisonValue = amount;
        gPoisonValue = gBasePoisonValue;
        gUpgradedPoisonValue = true;
        return gPoisonValue;
    }

    public int upgradeGStrValue(int amount)
    {
        gBaseStrValue = amount;
        gStrValue = gBaseStrValue;
        gUpgradedStrValue = true;
        return gStrValue;
    }

    //endregion
    //endregion

    //region Add Cards
    public void AddRandomCardHand(int time, CardType type)
    {
        for (int i = 0; i < time; i++)
        {
            AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();
            this.addToBot(new MakeTempCardInHandAction(c, true));
        }
    }

    public void AddRandomCardDiscard(int time, CardType type)
    {
        for (int i = 0; i < time; i++)
        {
            AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();
            this.addToBot(new MakeTempCardInDiscardAction(c, true));
        }
    }

    public void AddRandomColorless(int time, String where)
    {
        switch (where)
        {
            case "Hand":
                for (int i = 0; i < time; i++)
                {
                    AbstractCard c = AbstractDungeon.returnTrulyRandomColorlessCardInCombat(AbstractDungeon.cardRandomRng).makeCopy();
                    this.addToBot(new MakeTempCardInHandAction(c, true));
                }
                break;
            case "Discard":
                for (int i = 0; i < time; i++)
                {
                    AbstractCard c = AbstractDungeon.returnTrulyRandomColorlessCardInCombat(AbstractDungeon.cardRandomRng).makeCopy();
                    this.addToBot(new MakeTempCardInDiscardAction(c, true));
                }
                break;
            case "DrawPile":
                for (int i = 0; i < time; i++)
                {
                    AbstractCard c = AbstractDungeon.returnTrulyRandomColorlessCardInCombat(AbstractDungeon.cardRandomRng).makeCopy();

                    this.addToBot(new MakeTempCardInDrawPileAction(c, 1, true, true));
                }
                break;
            case "TopDrawPile":
                for (int i = 0; i < time; i++)
                {
                    AbstractCard c = AbstractDungeon.returnTrulyRandomColorlessCardInCombat(AbstractDungeon.cardRandomRng).makeCopy();

                    this.addToBot(new MakeTempCardInDrawPileAction(c, 1, false, false, false));
                }
                break;
            case "BotDrawPile":
                for (int i = 0; i < time; i++)
                {
                    AbstractCard c = AbstractDungeon.returnTrulyRandomColorlessCardInCombat(AbstractDungeon.cardRandomRng).makeCopy();

                    this.addToBot(new MakeTempCardInDrawPileAction(c, 1, false, false, true));
                }
                break;
            default:
                break;
        }
    }

    public void AddRandomCardDrawP(int time, CardType type, String position)
    {
        if (position.equals("Random"))
        {
            for (int i = 0; i < time; i++)
            {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();

                this.addToBot(new MakeTempCardInDrawPileAction(c, 1, true, true));
            }
        } else if (position.equals("Top"))
        {
            for (int i = 0; i < time; i++)
            {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();

                this.addToBot(new MakeTempCardInDrawPileAction(c, 1, false, false, false));
            }
        } else if (position.equals("Bot"))
        {
            for (int i = 0; i < time; i++)
            {
                AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();

                this.addToBot(new MakeTempCardInDrawPileAction(c, 1, false, false, true));
            }
        }
    }


    public void AddRandomCardHandCopy(int time, CardType type)
    {
        AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();
        this.addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy(), time, true));
    }


    public void AddRandomCardDiscardCopy(int time, CardType type)
    {

        AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();
        for (int i = 0; i < time; i++)
        {
            this.addToBot(new MakeTempCardInDiscardAction(c.makeStatEquivalentCopy(), true));
        }

    }

    public void AddRandomCardDrawPCopy(int time, CardType type, String position)
    {
        AbstractCard c = AbstractDungeon.returnTrulyRandomCardInCombat(type).makeCopy();

        if (position.equals("Random"))
        {
            this.addToBot(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), time, true, true));

        } else if (position.equals("Top"))
        {
            this.addToBot(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), time, false, false, false));

        } else if (position.equals("Bot"))
        {
            this.addToBot(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), time, false, false, true));

        }
    }

    public void AddRandomColorlessCopy(int time, String where)
    {
        AbstractCard c = AbstractDungeon.returnTrulyRandomColorlessCardInCombat(AbstractDungeon.cardRandomRng).makeCopy();

        switch (where)
        {
            case "Hand":
                this.addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy(), time, true));
                break;
            case "Discard":
                for (int i = 0; i < time; i++)
                {
                    this.addToBot(new MakeTempCardInDiscardAction(c.makeStatEquivalentCopy(), true));
                }
                break;
            case "DrawPile":
                this.addToBot(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), time, true, true));
                break;
            case "TopDrawPile":
                this.addToBot(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), time, false, false, false));
                break;
            case "BotDrawPile":
                this.addToBot(new MakeTempCardInDrawPileAction(c.makeStatEquivalentCopy(), time, false, false, true));
                break;
            default:
                break;
        }
    }

    //endregion

    //region baseMultiDmg

    //region PowerApply
    AbstractPlayer player;

    //region single Apply utils
    float tmpDmg;
    float tmpDPE;
    float tmpVampDmg;
    float tmpDmgPsnCon;
    float tmpDPAP;
    float tmpDPSH;

    float tmpArray[];

    Iterator var3;
    AbstractPower pApply;

    public void DmgModifySetFalse()
    {
        this.isDamageModified = false;
        this.isDPEModified = false;
        this.isVampDmgModified = false;
        this.isDmgPsnConditionModified = false;
        this.isDPAPModified = false;
        this.isDPSHModified = false;

    }

    public void SetTmp()
    {
        tmpDmg = (float) this.baseDamage;
        tmpDPE = (float) this.baseDmgPerEnergy;
        tmpVampDmg = (float) this.baseVampDmg;
        tmpDmgPsnCon = (float) this.baseDmgPsnCondition;
        tmpDPAP = (float) this.baseDPAP;
        tmpDPSH = (float) this.baseDPSH;

        tmpArray = new float[]{
                tmpDmg,
                tmpDPE,
                tmpVampDmg,
                tmpDmgPsnCon,
                tmpDPAP,
                tmpDPSH
        };
    }

    public void SetRelicModifier()
    {
        var3 = player.relics.iterator();

        while (var3.hasNext())
        {
            AbstractRelic r = (AbstractRelic) var3.next();
            for (int i = 0; i < tmpArray.length; i++)
            {
                tmpArray[i] = r.atDamageModify(tmpArray[i], this);
            }
            SetDmgModifierBoolLoop();

            //endregion
        }
    }

    public void SetDmgModifierBoolLoop()
    {
        for (int i = 0; i < tmpArray.length; i++)
        {
            if (baseDmgArray[i] != (int) tmpArray[i])
            {
                dmgBoolArray[i] = true;
            }
        }
        SetDmgModifierBool();
    }

    int counter;

    public void SetDmgModifierBool()
    {
        counter = 0;
        this.isDamageModified = dmgBoolArray[counter++];
        this.isDPEModified = dmgBoolArray[counter++];
        this.isVampDmgModified = dmgBoolArray[counter++];
        this.isDmgPsnConditionModified = dmgBoolArray[counter++];
        this.isDPAPModified = dmgBoolArray[counter++];
        this.isDPSHModified = dmgBoolArray[counter++];

    }

    public void AtDmgGiveLoop()
    {
        for (int i = 0; i < tmpArray.length; i++)
        {
            for (var3 = player.powers.iterator(); var3.hasNext(); tmpArray[i] = pApply.atDamageGive(tmpArray[i], this.damageTypeForTurn, this))
            {
                pApply = (AbstractPower) var3.next();
            }
        }

    }

    public void AtDmgGive()
    {

        for (int i = 0; i < tmpArray.length; i++)
        {
            tmpArray[i] = player.stance.atDamageGive(tmpArray[i], this.damageTypeForTurn, this);
        }
        SetDmgModifierBoolLoop();
    }

    public void AtDmgFinalLoop()
    {
        for (int i = 0; i < tmpArray.length; i++)
        {
            for (var3 = player.powers.iterator(); var3.hasNext(); tmpArray[i] = pApply.atDamageFinalGive(tmpArray[i], this.damageTypeForTurn, this))
            {
                pApply = (AbstractPower) var3.next();
            }
        }
    }

    public void SetTmpZero()
    {

        for (int i = 0; i < tmpArray.length; i++)
        {
            if (tmpArray[i] < 0.0F)
                tmpArray[i] = 0.0F;
        }
    }

    int dmgCounter;

    public void SetDmgFinal()
    {
        SetTmpZero();

        for (int i = 0; i < tmpArray.length; i++)
        {
            if (baseDmgArray[i] != MathUtils.floor(tmpArray[i]))
            {
                dmgBoolArray[i] = true;
            }

            DMGArray[i] = MathUtils.floor(tmpArray[i]);
        }
        SetDmgModifierBool();
        dmgCounter = 0;
        this.damage = DMGArray[dmgCounter++];
        this.dmgPerEnergy = DMGArray[dmgCounter++];
        this.vampDmg = DMGArray[dmgCounter++];
        this.dmgPsnCondition = DMGArray[dmgCounter++];
        this.dmgPerAttPlayed = DMGArray[dmgCounter++];
        this.dmgPerSkillInHand = DMGArray[dmgCounter++];
    }


    //endregion

    //region multi Apply utils
    float tmpMonsterCount[];
    float tmpDmgMulti[];
    float tmpDPEMulti[];
    float tmpVampDmgMulti[];

    ArrayList<AbstractMonster> m;

    Iterator var5;
    AbstractPower pMultiApply;

    public void InitTmpMulti()
    {
        m = AbstractDungeon.getCurrRoom().monsters.monsters;
        tmpMonsterCount = new float[m.size()];
        tmpDmgMulti = new float[m.size()];
        tmpDPEMulti = new float[m.size()];
        tmpVampDmgMulti = new float[m.size()];
    }

    public void SetTmpMulti(int i)
    {
        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            tmpDmgMulti[i] = (float) this.baseDamage;
            tmpDPEMulti[i] = (float) this.baseDmgPerEnergy;
            tmpVampDmgMulti[i] = (float) this.baseVampDmg;
        }
    }

    public void SetDmgModifierMultiBool(int i)
    {
        if (this.baseDamage != (int) tmpDmgMulti[i])
        {
            this.isDamageModified = true;
        }
        if (this.baseDmgPerEnergy != (int) tmpDPEMulti[i])
        {
            this.isDPEModified = true;
        }
        if (this.baseVampDmg != (int) tmpVampDmgMulti[i])
        {
            this.isVampDmgModified = true;
        }
    }

    public void SetRelicModifierMulti(int i)
    {
        var5 = player.relics.iterator();

        while (var5.hasNext())
        {
            AbstractRelic r = (AbstractRelic) var5.next();
            tmpDmgMulti[i] = r.atDamageModify(tmpDmgMulti[i], this);
            tmpDPEMulti[i] = r.atDamageModify(tmpDPEMulti[i], this);
            tmpVampDmgMulti[i] = r.atDamageModify(tmpVampDmgMulti[i], this);

            SetDmgModifierMultiBool(i);
        }
    }

    public void AtDmgGiveLoopMulti(int i)
    {
        for (var5 = player.powers.iterator(); var5.hasNext(); tmpDmgMulti[i] = pMultiApply.atDamageGive(tmpDmgMulti[i], this.damageTypeForTurn, this))
        {
            pMultiApply = (AbstractPower) var5.next();
        }
        for (var5 = player.powers.iterator(); var5.hasNext(); tmpDPEMulti[i] = pMultiApply.atDamageGive(tmpDPEMulti[i], this.damageTypeForTurn, this))
        {
            pMultiApply = (AbstractPower) var5.next();
        }
        for (var5 = player.powers.iterator(); var5.hasNext(); tmpVampDmgMulti[i] = pMultiApply.atDamageGive(tmpVampDmgMulti[i], this.damageTypeForTurn, this))
        {
            pMultiApply = (AbstractPower) var5.next();
        }
    }

    public void AtDmgGiveMulti(int i)
    {
        tmpDmgMulti[i] = player.stance.atDamageGive(tmpDmgMulti[i], this.damageTypeForTurn, this);
        tmpDPEMulti[i] = player.stance.atDamageGive(tmpDPEMulti[i], this.damageTypeForTurn, this);
        tmpVampDmgMulti[i] = player.stance.atDamageGive(tmpVampDmgMulti[i], this.damageTypeForTurn, this);

        SetDmgModifierMultiBool(i);
    }

    public void AtDmgFinalLoopMulti(int i)
    {
        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            for (var5 = player.powers.iterator(); var5.hasNext(); tmpDmgMulti[i] = pMultiApply.atDamageFinalGive(tmpDmgMulti[i], this.damageTypeForTurn, this))
            {
                pMultiApply = (AbstractPower) var5.next();
            }
            for (var5 = player.powers.iterator(); var5.hasNext(); tmpDPEMulti[i] = pMultiApply.atDamageFinalGive(tmpDPEMulti[i], this.damageTypeForTurn, this))
            {
                pMultiApply = (AbstractPower) var5.next();
            }
            for (var5 = player.powers.iterator(); var5.hasNext(); tmpVampDmgMulti[i] = pMultiApply.atDamageFinalGive(tmpVampDmgMulti[i], this.damageTypeForTurn, this))
            {
                pMultiApply = (AbstractPower) var5.next();
            }
        }
    }

    public void SetTmpZeroMulti(int i)
    {
        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            if (tmpDmgMulti[i] < 0.0F)
            {
                tmpDmgMulti[i] = 0.0F;
            }
            if (tmpDPEMulti[i] < 0.0F)
            {
                tmpDPEMulti[i] = 0.0F;
            }
            if (tmpVampDmgMulti[i] < 0.0F)
            {
                tmpVampDmgMulti[i] = 0.0F;
            }
        }
        this.multiDamage = new int[tmpDmgMulti.length];
        this.multiDPE = new int[tmpDPEMulti.length];
        this.multiVampDmg = new int[tmpVampDmgMulti.length];
    }

    public void SetDmgFinalMulti(int i)
    {
        SetTmpZeroMulti(i);

        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            this.multiDamage[i] = MathUtils.floor(tmpDmgMulti[i]);
            this.multiDPE[i] = MathUtils.floor(tmpDPEMulti[i]);
            this.multiVampDmg[i] = MathUtils.floor(tmpVampDmgMulti[i]);

            SetDmgModifierMultiBool(i);
        }

        this.damage = this.multiDamage[0];
        this.dmgPerEnergy = this.multiDPE[0];
        this.vampDmg = this.multiVampDmg[0];
    }

    //endregion
    public void applyPowers()
    {
        this.applyPowersToBlock();
        player = AbstractDungeon.player;

        DmgModifySetFalse();

        SetTmp();

        SetRelicModifier();

        AtDmgGiveLoop();

        AtDmgGive();

        AtDmgFinalLoop();

        SetDmgFinal();

        //something should be here to skip this calculation to save resources

        if (this.isMultiDamage)
        {
            InitTmpMulti();
            int i = 0;
            SetTmpMulti(i);

            for (i = 0; i < tmpMonsterCount.length; ++i)
            {
                SetRelicModifierMulti(i);
                AtDmgGiveLoopMulti(i);

                AtDmgGiveMulti(i);

            }
            AtDmgFinalLoopMulti(i);
            SetDmgFinalMulti(i);
        }

    }
    //endregion

    //region DmgCal

    //region dmgCal Single
    AbstractPower pCal;
    Iterator var9;

    public void SetRelicModifierCal()
    {
        var9 = player.relics.iterator();

        while (var9.hasNext())
        {
            AbstractRelic r = (AbstractRelic) var9.next();

            for (int i = 0; i < tmpArray.length; i++)
            {
                tmpArray[i] = r.atDamageModify(tmpArray[i], this);
            }
            SetDmgModifierBoolLoop();

            //endregion
        }
    }

    public void AtDmgGiveLoopCal()
    {
        for (int i = 0; i < tmpArray.length; i++)
        {
            for (var9 = player.powers.iterator(); var9.hasNext(); tmpArray[i] = pCal.atDamageGive(tmpArray[i], this.damageTypeForTurn, this))
            {
                pCal = (AbstractPower) var9.next();
            }
        }
    }

    public void AtDmgCal(AbstractMonster mo)
    {
        for (int i = 0; i < tmpArray.length; i++)
        {
            tmpArray[i] = player.stance.atDamageGive(tmpArray[i], this.damageTypeForTurn, this);
            if (baseDmgArray[i] != (int) tmpArray[i])
            {
                dmgBoolArray[i] = true;
            }

            for (var9 = mo.powers.iterator(); var9.hasNext(); tmpArray[i] = pCal.atDamageReceive(tmpArray[i], this.damageTypeForTurn, this))
            {
                pCal = (AbstractPower) var9.next();
            }

            for (var9 = player.powers.iterator(); var9.hasNext(); tmpArray[i] = pCal.atDamageFinalGive(tmpArray[i], this.damageTypeForTurn, this))
            {
                pCal = (AbstractPower) var9.next();
            }

            for (var9 = mo.powers.iterator(); var9.hasNext(); tmpArray[i] = pCal.atDamageFinalReceive(tmpArray[i], this.damageTypeForTurn, this))
            {
                pCal = (AbstractPower) var9.next();
            }
        }
        SetDmgModifierBool();

    }
    //endregion

    //region dmgCal Multi
    Iterator var6;
    AbstractPower pCalMulti;

    public void SetRelicModifierMultiCal(int i)
    {
        var6 = player.relics.iterator();

        while (var6.hasNext())
        {
            AbstractRelic r = (AbstractRelic) var6.next();
            tmpDmgMulti[i] = r.atDamageModify(tmpDmgMulti[i], this);
            tmpDPEMulti[i] = r.atDamageModify(tmpDPEMulti[i], this);
            tmpVampDmgMulti[i] = r.atDamageModify(tmpVampDmgMulti[i], this);

            SetDmgModifierMultiBool(i);

        }
    }

    public void AtDmgGiveLoopMultiCal(int i)
    {
        for (var6 = player.powers.iterator(); var6.hasNext(); tmpDmgMulti[i] = pCalMulti.atDamageGive(tmpDmgMulti[i], this.damageTypeForTurn, this))
        {
            pCalMulti = (AbstractPower) var6.next();
        }
        for (var6 = player.powers.iterator(); var6.hasNext(); tmpDPEMulti[i] = pCalMulti.atDamageGive(tmpDPEMulti[i], this.damageTypeForTurn, this))
        {
            pCalMulti = (AbstractPower) var6.next();
        }
        for (var6 = player.powers.iterator(); var6.hasNext(); tmpVampDmgMulti[i] = pCalMulti.atDamageGive(tmpVampDmgMulti[i], this.damageTypeForTurn, this))
        {
            pCalMulti = (AbstractPower) var6.next();
        }
    }

    public void AtDmgReceiveLoop(int i)
    {
        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            var6 = ((AbstractMonster) m.get(i)).powers.iterator();

            while (var6.hasNext())
            {
                pCalMulti = (AbstractPower) var6.next();
                if (!((AbstractMonster) m.get(i)).isDying && !((AbstractMonster) m.get(i)).isEscaping)
                {
                    tmpDmgMulti[i] = pCalMulti.atDamageReceive(tmpDmgMulti[i], this.damageTypeForTurn, this);
                    tmpDPEMulti[i] = pCalMulti.atDamageReceive(tmpDPEMulti[i], this.damageTypeForTurn, this);
                    tmpVampDmgMulti[i] = pCalMulti.atDamageReceive(tmpVampDmgMulti[i], this.damageTypeForTurn, this);
                }
            }
        }
    }

    public void AtDmgFinalLoopMultiCal(int i)
    {
        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            for (var6 = player.powers.iterator(); var6.hasNext(); tmpDmgMulti[i] = pCalMulti.atDamageFinalGive(tmpDmgMulti[i], this.damageTypeForTurn, this))
            {
                pCalMulti = (AbstractPower) var6.next();
            }
            for (var6 = player.powers.iterator(); var6.hasNext(); tmpDPEMulti[i] = pCalMulti.atDamageFinalGive(tmpDPEMulti[i], this.damageTypeForTurn, this))
            {
                pCalMulti = (AbstractPower) var6.next();
            }
            for (var6 = player.powers.iterator(); var6.hasNext(); tmpVampDmgMulti[i] = pCalMulti.atDamageFinalGive(tmpVampDmgMulti[i], this.damageTypeForTurn, this))
            {
                pCalMulti = (AbstractPower) var6.next();
            }
        }
    }

    public void AtDmgFinalReceiveCal(int i)
    {
        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            var6 = ((AbstractMonster) m.get(i)).powers.iterator();

            while (var6.hasNext())
            {
                pCalMulti = (AbstractPower) var6.next();
                if (!((AbstractMonster) m.get(i)).isDying && !((AbstractMonster) m.get(i)).isEscaping)
                {
                    tmpDmgMulti[i] = pCalMulti.atDamageFinalReceive(tmpDmgMulti[i], this.damageTypeForTurn, this);
                    tmpDPEMulti[i] = pCalMulti.atDamageFinalReceive(tmpDPEMulti[i], this.damageTypeForTurn, this);
                    tmpVampDmgMulti[i] = pCalMulti.atDamageFinalReceive(tmpVampDmgMulti[i], this.damageTypeForTurn, this);
                }

            }
        }
    }

    public void DmgFloorBool(int i)
    {
        if (this.baseDamage != MathUtils.floor(tmpDmgMulti[i]))
        {
            this.isDamageModified = true;
        }

        if (this.baseDmgPerEnergy != MathUtils.floor(tmpDPEMulti[i]))
        {
            this.isDPEModified = true;
        }

        if (this.baseVampDmg != MathUtils.floor(tmpVampDmgMulti[i]))
        {
            this.isVampDmgModified = true;
        }
    }

    public void SetFinalDmgMultiCal(int i)
    {
        SetTmpZeroMulti(i);
        for (i = 0; i < tmpMonsterCount.length; ++i)
        {
            DmgFloorBool(i);

            this.multiDamage[i] = MathUtils.floor(tmpDmgMulti[i]);
            this.multiDPE[i] = MathUtils.floor(tmpDPEMulti[i]);
            this.multiVampDmg[i] = MathUtils.floor(tmpVampDmgMulti[i]);
        }

        this.damage = this.multiDamage[0];
        this.dmgPerEnergy = this.multiDPE[0];
        this.vampDmg = this.multiVampDmg[0];
    }

    //endregion

    public void calculateCardDamage(AbstractMonster mo)
    {
        this.applyPowersToBlock();
        player = AbstractDungeon.player;

        DmgModifySetFalse();

        if (mo != null)
        {
            SetTmp();

            SetRelicModifierCal();

            AtDmgGiveLoopCal();

            AtDmgCal(mo);

            SetDmgFinal();

        }

        //something should be here to skip this calculation to save resources

        if (this.isMultiDamage && mo != null)
        {
            InitTmpMulti();
            int i = 0;

            SetTmpMulti(i);

            for (i = 0; i < tmpMonsterCount.length; ++i)
            {
                SetRelicModifierMultiCal(i);

                AtDmgGiveLoopMultiCal(i);

                AtDmgGiveMulti(i);

                AtDmgReceiveLoop(i);

                AtDmgFinalLoopMultiCal(i);

                AtDmgFinalReceiveCal(i);

                SetFinalDmgMultiCal(i);
            }

        }
    }
    //endregion

    //endregion

    /*---------------------------------------------------------------------------*/

}
