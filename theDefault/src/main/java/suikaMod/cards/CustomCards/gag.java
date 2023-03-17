package suikaMod.cards.CustomCards;

import basemod.AutoAdd;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.*;
import com.megacrit.cardcrawl.actions.defect.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import suikaMod.DefaultMod;
import suikaMod.actions.*;
import suikaMod.cards.AbstractDynamicCard;
import suikaMod.characters.TheDefault;

import static suikaMod.DefaultMod.makeCardPath;

import java.io.File;  // Import the File class
import java.io.IOException;
import java.util.Iterator;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;


@AutoAdd.Seen
public class gag extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(gag.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 1;
    private static final int UPGRADE_DAMAGE = 1;
    private int dmgModifyValue = 1;
    private final int UPGRADE_dmgModifyValue = 1;
    private static final int dmgPerEnergyValue = 1;
    private static final int UPGRADE_dmgPerEnergyValue = 1;
    private static final int vampDmgValue = 1;
    private static final int UPGRADE_vampDmgValue = 1;
    private int repeatTIME = 1;
    private final int UPGRADE_repeatTIME = 1;

    // /STAT DECLARATION/

    private static String desc = "a";

    public gag() {
        super(ID, "gag", IMG, desc, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseDmgPerEnergy = dmgPerEnergyValue;
        baseVampDmg = vampDmgValue;

        //this.tags.add(CardTags.STARTER_STRIKE);

    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_HORIZONTAL));
        this.addToBot(new ModifyDmgAction(this.uuid, dmgModifyValue));
        if (m != null && m.getIntentBaseDmg() >= 0) {
            for (int i = 0; i < repeatTIME; i++) {
                this.addToBot(
                        new SkewerAction(p, m, dmgPerEnergy, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
                this.addToBot(
                        new VampireDamageAction(m, new DamageInfo(p, vampDmg, damageTypeForTurn), AttackEffect.NONE));
            }
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            dmgModifyValue = UPGRADE_dmgModifyValue;
            upgradeDPE(UPGRADE_dmgPerEnergyValue);
            upgradeVampDmg(UPGRADE_vampDmgValue);
            repeatTIME = UPGRADE_repeatTIME;
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}