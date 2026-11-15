package com.emojitcg.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val bg=Color(0xFF10111B); private val panel=Color(0xFF1B1D2B); private val violet=Color(0xFF9B7BFF); private val gold=Color(0xFFFFC857); private val muted=Color(0xFF9B9DB0)
data class CardData(val id:Int,val name:String,val emoji:String,val element:String,val rarity:String,val hp:Int,val atk:Int,val move:String)
private val allCards=listOf(
 CardData(1,"Inferno","🔥","FIRE","RARE",120,60,"Flame Burst"),CardData(2,"Frosty","🥶","ICE","EPIC",150,75,"Freeze Blast"),CardData(3,"Rich Guy","🤑","GOLD","LEGENDARY",180,90,"Golden Strike"),CardData(4,"Demon","😈","DARK","MYTHICAL",200,100,"Shadow Crash"),CardData(5,"Gigachad","🗿","EARTH","RARE",140,55,"Stone Face"),CardData(6,"Lightning","⚡","STORM","EPIC",130,80,"Thunderbolt"),CardData(7,"Sneaky","🥷","DARK","RARE",110,65,"Ninja Rush"),CardData(8,"Angel","😇","LIGHT","LEGENDARY",170,85,"Heavenly Ray"),CardData(9,"Goblin","👺","FIRE","COMMON",90,35,"Bonk"),CardData(10,"Goofy","🤡","CHAOS","COMMON",100,30,"Clown Around"),CardData(11,"Robot","🤖","TECH","COMMON",105,40,"Laser Ping"),CardData(12,"Alien","👽","COSMIC","EPIC",145,70,"UFO Beam"))
private fun rarity(r:String)=when(r){"MYTHICAL"->Color(0xFFFF65B5);"LEGENDARY"->gold;"EPIC"->violet;"RARE"->Color(0xFF59B8FF);else->Color(0xFF9AA0AA)}

class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{GameApp()}}}

@Composable fun GameApp(){
 var tab by rememberSaveable{mutableStateOf("Home")};var coins by rememberSaveable{mutableIntStateOf(500)};var owned by rememberSaveable{mutableStateOf(listOf(1,5,9,10,11))};var activeId by rememberSaveable{mutableIntStateOf(1)};var wins by rememberSaveable{mutableIntStateOf(0)}
 var started by rememberSaveable{mutableStateOf(false)};var playerHp by rememberSaveable{mutableIntStateOf(120)};var enemyHp by rememberSaveable{mutableIntStateOf(130)};var message by rememberSaveable{mutableStateOf("Ready when you are.")}
 val active=allCards.first{it.id==activeId}
 MaterialTheme(colorScheme=darkColorScheme(primary=violet,background=bg,surface=panel)){
 Scaffold(containerColor=bg,bottomBar={NavigationBar(containerColor=panel){listOf("Home","Cards","Battle","Packs","Shop").forEach{item->NavigationBarItem(selected=tab==item,onClick={tab=item},icon={Icon(when(item){"Home"->Icons.Default.Home;"Cards"->Icons.Default.Collections;"Battle"->Icons.Default.SportsEsports;"Packs"->Icons.Default.CardGiftcard;else->Icons.Default.Store},null)},label={Text(item)},colors=NavigationBarItemDefaults.colors(selectedIconColor=violet,selectedTextColor=violet,indicatorColor=violet.copy(alpha=.15f),unselectedIconColor=muted,unselectedTextColor=muted))}}}){pad->
 Column(Modifier.fillMaxSize().padding(pad).padding(horizontal=18.dp)){
 Row(Modifier.fillMaxWidth().padding(vertical=15.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("EMOJI TCG",color=Color.White,fontWeight=FontWeight.Black,fontSize=23.sp,letterSpacing=1.5.sp);Text("COLLECT • BUILD • BATTLE",color=muted,fontSize=10.sp,letterSpacing=2.sp)};Surface(color=panel,shape=RoundedCornerShape(30.dp)){Text("🪙 "+coins,Modifier.padding(horizontal=14.dp,vertical=10.dp),color=gold,fontWeight=FontWeight.Bold)}}
 when(tab){
 "Home"->LazyColumn(verticalArrangement=Arrangement.spacedBy(14.dp),contentPadding=PaddingValues(bottom=16.dp)){
 item{Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(listOf(Color(0xFF392A70),Color(0xFF171B36)))).padding(20.dp)){Column{Text("THE EMOJI ARENA",color=Color(0xFFD8CEFF),fontSize=10.sp,letterSpacing=2.sp);Text("Build your\nultimate squad.",color=Color.White,fontSize=31.sp,fontWeight=FontWeight.Black,lineHeight=35.sp);Spacer(Modifier.height(14.dp));Button(onClick={tab="Battle"},colors=ButtonDefaults.buttonColors(containerColor=violet)){Text("BATTLE NOW →",color=Color.White,fontWeight=FontWeight.Bold)}}}}
 item{Row(horizontalArrangement=Arrangement.spacedBy(9.dp)){Stat("CARDS",owned.size.toString()+"/12","🃏",Modifier.weight(1f));Stat("WINS",wins.toString(),"🏆",Modifier.weight(1f));Stat("COINS",coins.toString(),"🪙",Modifier.weight(1f))}}
 item{Text("YOUR SQUAD",color=Color.White,fontWeight=FontWeight.Bold,letterSpacing=1.sp)}
 item{Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(10.dp)){owned.mapNotNull{id->allCards.find{it.id==id}}.forEach{MiniCard(it)}}}
 item{Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){Action("🎁","Open a pack","Find a new card",{tab="Packs"},Modifier.weight(1f));Action("⚔️","Battle a bot","Earn 120 coins",{tab="Battle"},Modifier.weight(1f))}}
 }
 "Cards"->Column{Text("Your collection",color=Color.White,fontSize=25.sp,fontWeight=FontWeight.Black);Text(owned.size.toString()+" cards collected • Tap to select your fighter",color=muted,fontSize=12.sp);Spacer(Modifier.height(12.dp));LazyColumn(verticalArrangement=Arrangement.spacedBy(9.dp)){items(owned.mapNotNull{id->allCards.find{it.id==id}}){c->Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(if(c.id==activeId)Color(0xFF30265F)else panel).border(if(c.id==activeId)1.dp else 0.dp,violet,RoundedCornerShape(17.dp)).clickable{activeId=c.id}.padding(13.dp),verticalAlignment=Alignment.CenterVertically){Text(c.emoji,fontSize=38.sp);Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(c.name,color=Color.White,fontWeight=FontWeight.Bold);Text(c.element+" • "+c.rarity,color=rarity(c.rarity),fontSize=10.sp);Text(c.move,color=muted,fontSize=11.sp)};Column(horizontalAlignment=Alignment.End){Text(c.hp.toString()+" HP",color=Color.White,fontWeight=FontWeight.Bold);Text("⚔ "+c.atk,color=gold)}}}}}
 "Battle"->Column(horizontalAlignment=Alignment.CenterHorizontally){Text("BOT ARENA",color=Color.White,fontWeight=FontWeight.Black,fontSize=25.sp);Text("Offline practice battle",color=muted,fontSize=11.sp);Spacer(Modifier.height(18.dp));Fighter("🤡","GOOFY BOT",enemyHp,130);Text("VS",color=violet,fontWeight=FontWeight.Black,fontSize=23.sp,modifier=Modifier.padding(10.dp));Fighter(active.emoji,active.name.uppercase()+" • YOU",playerHp,active.hp);Spacer(Modifier.height(14.dp));Text(message,color=Color.White,textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(panel).padding(12.dp),fontSize=12.sp);Spacer(Modifier.height(12.dp))
 if(!started||playerHp==0||enemyHp==0)Button(onClick={started=true;playerHp=active.hp;enemyHp=130;message="Go! Use your attack."},modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=violet)){Text("START NEW BATTLE")}
 else Button(onClick={enemyHp=(enemyHp-active.atk).coerceAtLeast(0);if(enemyHp==0){coins+=120;wins++;message="VICTORY! +120 coins. Amazing!";started=false}else{playerHp=(playerHp-28).coerceAtLeast(0);if(playerHp==0){message="DEFEAT! Try again.";started=false}else message=active.move+" dealt "+active.atk+" damage. Bot hit back for 28."}},modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFE45D68))){Text("⚔ ATTACK",fontWeight=FontWeight.Black)}
 TextButton(onClick={tab="Cards"}){Text("Choose another card",color=muted)}}
 "Packs"->Column(horizontalAlignment=Alignment.CenterHorizontally){Text("BOOSTER PACKS",color=Color.White,fontSize=24.sp,fontWeight=FontWeight.Black);Text("One new emoji character in every pack",color=muted,fontSize=11.sp);Spacer(Modifier.height(22.dp));Box(Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(24.dp)).background(Brush.linearGradient(listOf(Color(0xFF5A3DA0),Color(0xFF171B36)))),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("✨",fontSize=32.sp);Text("EMOJI",color=Color.White,fontSize=35.sp,fontWeight=FontWeight.Black,letterSpacing=4.sp);Text("POWER PACK",color=gold,fontWeight=FontWeight.Bold,letterSpacing=3.sp);Text("1 CARD • 1 SURPRISE",color=Color.White.copy(alpha=.7f),fontSize=10.sp)}};Spacer(Modifier.height(18.dp));Button(onClick={val remaining=allCards.filter{it.id !in owned};if(coins>=150&&remaining.isNotEmpty()){coins-=150;val roll=Random.nextInt(100);val tier=when{roll<3->"MYTHICAL";roll<12->"LEGENDARY";roll<35->"EPIC";roll<70->"RARE";else->"COMMON"};val prize=remaining.filter{it.rarity==tier}.ifEmpty{remaining}.random();owned=owned+prize.id;activeId=prize.id;message="You pulled "+prize.emoji+" "+prize.name+"!"}else if(remaining.isEmpty())message="You collected every card! More coming soon."else message="You need 150 coins."},enabled=coins>=150&&owned.size<allCards.size,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=violet)){Text("OPEN PACK • 150 🪙",fontWeight=FontWeight.Bold)};Text("Common 30% • Rare 35% • Epic 23% • Legendary 9% • Mythical 3%",color=muted,fontSize=10.sp,textAlign=TextAlign.Center,modifier=Modifier.padding(top=12.dp));Text(message,color=gold,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=15.dp));Text("Packs use earned in-game coins only. No real-money purchases.",color=muted,fontSize=10.sp,textAlign=TextAlign.Center,modifier=Modifier.padding(top=12.dp))}
 else->Column{Text("THE SHOP",color=Color.White,fontSize=25.sp,fontWeight=FontWeight.Black);Text("Free rewards for your next pack",color=muted,fontSize=12.sp);Spacer(Modifier.height(16.dp));Action("🎁","Daily bonus","Claim 100 free coins",{coins+=100;message="100 coins added!"},Modifier.fillMaxWidth());Spacer(Modifier.height(12.dp));Action("🏆","Battle record",wins.toString()+" victories so far",{},Modifier.fillMaxWidth());Spacer(Modifier.height(12.dp));Text("Coins: "+coins,color=gold,fontWeight=FontWeight.Bold)}
 }
 }
 }}
}

@Composable fun Stat(label:String,value:String,emoji:String,modifier:Modifier){Column(modifier.clip(RoundedCornerShape(15.dp)).background(panel).padding(11.dp)){Text(emoji,fontSize=19.sp);Text(value,color=Color.White,fontWeight=FontWeight.Bold,fontSize=16.sp);Text(label,color=muted,fontSize=9.sp,letterSpacing=1.sp)}}
@Composable fun MiniCard(c:CardData){Column(Modifier.width(105.dp).clip(RoundedCornerShape(15.dp)).background(Brush.linearGradient(listOf(rarity(c.rarity).copy(alpha=.35f),panel))).border(1.dp,rarity(c.rarity).copy(alpha=.7f),RoundedCornerShape(15.dp)).padding(10.dp)){Text(c.emoji,fontSize=34.sp);Text(c.name,color=Color.White,fontWeight=FontWeight.Bold,fontSize=12.sp);Text(c.rarity,color=rarity(c.rarity),fontSize=9.sp,fontWeight=FontWeight.Bold);Text(c.hp.toString()+" HP",color=muted,fontSize=10.sp)}}
@Composable fun Action(emoji:String,title:String,subtitle:String,onClick:()->Unit,modifier:Modifier){Column(modifier.clip(RoundedCornerShape(16.dp)).background(panel).clickable{onClick()}.padding(14.dp)){Text(emoji,fontSize=26.sp);Text(title,color=Color.White,fontWeight=FontWeight.Bold);Text(subtitle,color=muted,fontSize=11.sp)}}
@Composable fun Fighter(emoji:String,name:String,hp:Int,max:Int){Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(19.dp)).background(panel).padding(15.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(emoji,fontSize=44.sp);Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(name,color=Color.White,fontWeight=FontWeight.Bold);Text("HP "+hp+" / "+max,color=muted,fontSize=11.sp)}};Spacer(Modifier.height(9.dp));LinearProgressIndicator(progress={hp.toFloat()/max.coerceAtLeast(1)},modifier=Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp)),color=if(hp<40)Color(0xFFFF6262)else Color(0xFF62D69A),trackColor=bg)}}
