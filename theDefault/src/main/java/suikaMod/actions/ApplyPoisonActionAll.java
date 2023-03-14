package suikaMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

import java.util.Iterator;

public class ApplyPoisonActionAll extends AbstractGameAction
{

    public ApplyPoisonActionAll(int stackAmount)
    {
        this.amount = stackAmount;
    }

    @Override
    public void update()
    {
        AbstractMonster mo;
        Iterator var3;
        var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        while (var3.hasNext())
        {
            mo = (AbstractMonster) var3.next();
            this.addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new PoisonPower(mo, AbstractDungeon.player, amount), amount, AttackEffect.POISON));
        }
        this.isDone = true;
    }
}