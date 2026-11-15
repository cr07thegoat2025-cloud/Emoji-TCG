package com.emojitcg.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val bg=Color(0xFFF4F0E8); private val paper=Color(0xFFFFFCF6); private val ink=Color(0xFF20211F); private val muted=Color(0xFF88877F); private val accent=Color(0xFFD86D52); private val edge=Color(0xFFE3DED3)
data class GameCard(val id:Int,val emoji:String,val name:String,val type:String,val hp:Int,val attack:Int,val rarity:String)
private val seeds=listOf(
"😀" to "Sunny","😎" to "Cool Cat","😂" to "Giggles","🥹" to "Softie","🤩" to "Starstruck","😈" to "Little Devil","👻" to "Ghost","💀" to "Skully","🤖" to "Byte","👽" to "Visitor","🤡" to "Big Top","💩" to "Mr Stink","👹" to "Oni","👺" to "Goblin","🤠" to "Cowpoke","🥷" to "Shadow","🧙" to "Wizard","🧛" to "Vampire","🧟" to "Walker","🧜" to "Siren","🦸" to "Hero","🦹" to "Rogue","🧑‍🚀" to "Cosmo","🧑‍🎤" to "Rockstar","🧑‍🍳" to "Chef",
"🔥" to "Blaze","💧" to "Drip","🌪️" to "Twister","⚡" to "Volt","❄️" to "Frost","🌈" to "Prism","☀️" to "Sol","🌙" to "Luna","⭐" to "Nova","🌑" to "Eclipse","🍀" to "Lucky","🌵" to "Prickles","🌻" to "Sunny Side","🍄" to "Mushroom","🌊" to "Tide",
"🐉" to "Dragon","🦊" to "Fox","🐺" to "Wolf","🦁" to "Lion","🐯" to "Tiger","🐸" to "Froggo","🐼" to "Panda","🐻" to "Bear","🐵" to "Monkey","🦄" to "Unicorn","🦋" to "Flutter","🐙" to "Eight","🦈" to "Chomper","🐢" to "Tank","🦖" to "Rex",
"🍕" to "Pizza Pal","🍉" to "Melon","🍩" to "Doughnut","🍓" to "Berry","🌮" to "Taco","🍔" to "Burger","🍟" to "Fries","🍪" to "Cookie","🍭" to "Lolly","🍜" to "Noodle","💎" to "Gem","👑" to "Royal","💰" to "Richie","🎯" to "Bullseye","🎸" to "Riff","🎮" to "Player One","🚀" to "Rocket","🛸" to "UFO","⚽" to "Striker","🏆" to "Champion",
"🗿" to "Stone Face","🎃" to "Pumpkin","🧸" to "Teddy","🪄" to "Magic Wand","🧿" to "Watcher","💥" to "Kaboom","❤️" to "Heartbeat","💜" to "Amethyst","🖤" to "Nightfall","💛" to "Goldie","🧊" to "Ice Cube","🪐" to "Orbit","🌋" to "Volcano","🏝️" to "Island","🌌" to "Galaxy","🦇" to "Batty","🕷️" to "Webber","🦂" to "Stinger","🐍" to "Slinky","🦍" to "Gorilla","🐬" to "Dolphin","🦉" to "Owl","🦚" to "Peacock","🦩" to "Flamingo","🐝" to "Buzz","🧠" to "Brainiac","👁️" to "All-Seeing","🫀" to "Pulse","🦾" to "Steel Arm","🧬" to "DNA"
)
private val cards=seeds.mapIndexed{i,p->GameCard(i,p.first,p.second,listOf("FIRE","WATER","NATURE","SHADOW","COSMIC","TECH")[i%6],80+(i%7)*15,25+(i%8)*9,when{ i%29==0->"MYTHIC";i%13==0->"LEGENDARY";i%5==0->"EPIC";i%2==0->"RARE";else->"COMMON"})}
private fun tier(r:String)=when(r){"MYTHIC"->Color(0xFF9D4EDD);"LEGENDARY"->Color(0xFFD19A38);"EPIC"->Color(0xFF5477B8);"RARE"->Color(0xFF4D8B79);else->muted}
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{EmojiGame()}}}

@Composable private fun EmojiGame(){
 var page by remember{mutableStateOf("Home")};var coins by remember{mutableIntStateOf(650)}
 var owned by remember{mutableStateOf(setOf(0,1,2,5,8,10,12,15,20,25,30,40))}
 var team by remember{mutableStateOf(listOf(0,1,2))};var wins by remember{mutableIntStateOf(0)}
 var note by remember{mutableStateOf("Welcome back. Build your team and play.")};var daily by remember{mutableStateOf(false)}
 var mode by remember{mutableStateOf("Practice bot")};var hp1 by remember{mutableIntStateOf(100)};var hp2 by remember{mutableIntStateOf(100)};var turn by remember{mutableIntStateOf(0)};var ended by remember{mutableStateOf(false)}
 val fighter=cards[team.firstOrNull()?:0]
 MaterialTheme(colorScheme=lightColorScheme(primary=accent,background=bg,surface=paper,onSurface=ink)){
 Scaffold(containerColor=bg,bottomBar={NavigationBar(containerColor=paper,tonalElevation=0.dp){listOf("Home","Cards","Battle","Packs","Shop").forEach{p->NavigationBarItem(selected=page==p,onClick={page=p},icon={Text(when(p){"Home"->"⌂";"Cards"->"▤";"Battle"->"⚔";"Packs"->"▧";else->"♧"},fontSize=20.sp)},label={Text(p,fontSize=10.sp)},colors=NavigationBarItemDefaults.colors(selectedIconColor=accent,selectedTextColor=accent,indicatorColor=accent.copy(alpha=.12f),unselectedIconColor=muted,unselectedTextColor=muted))}}}){pad->
 Column(Modifier.fillMaxSize().padding(pad)){
 Row(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=14.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("EMOJI CLUB",fontSize=21.sp,fontWeight=FontWeight.Black,letterSpacing=1.sp,color=ink);Text("COLLECT • TRADE • BATTLE",fontSize=9.sp,letterSpacing=1.5.sp,color=muted)};Surface(color=paper,shape=RoundedCornerShape(30.dp),border=BorderStroke(1.dp,edge)){Text("🪙 "+coins,Modifier.padding(horizontal=13.dp,vertical=8.dp),color=ink,fontWeight=FontWeight.Bold)}}
 when(page){
 "Home"->Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=20.dp)){
 Spacer(Modifier.height(8.dp));Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Color(0xFF292D2B)).padding(22.dp)){Column{Text("A COLLECTION WITH CHARACTER",color=Color(0xFFE8B58B),fontSize=9.sp,letterSpacing=1.5.sp);Spacer(Modifier.height(10.dp));Text("Find your favourites.\nBuild your dream team.",color=Color.White,fontSize=27.sp,fontWeight=FontWeight.Bold,lineHeight=32.sp);Spacer(Modifier.height(17.dp));Button(onClick={page="Battle"},colors=ButtonDefaults.buttonColors(containerColor=accent),shape=RoundedCornerShape(9.dp)){Text("Play a match  →",color=Color.White)}}}
 Spacer(Modifier.height(16.dp));Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){Metric("CARDS",owned.size.toString(),Modifier.weight(1f));Metric("TEAM",team.size.toString()+"/5",Modifier.weight(1f));Metric("WINS",wins.toString(),Modifier.weight(1f))}
 Spacer(Modifier.height(20.dp));Row(verticalAlignment=Alignment.CenterVertically){Text("YOUR TEAM",fontWeight=FontWeight.Bold,color=ink,modifier=Modifier.weight(1f));Text("EDIT",fontSize=10.sp,color=accent,fontWeight=FontWeight.Bold,modifier=Modifier.clickable{page="Cards"})}
 Spacer(Modifier.height(9.dp));Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){team.forEach{MiniCard(cards[it],Modifier.weight(1f))}}
 Spacer(Modifier.height(17.dp));Row(horizontalArrangement=Arrangement.spacedBy(9.dp)){Button(onClick={page="Packs"},modifier=Modifier.weight(1f),colors=ButtonDefaults.buttonColors(containerColor=accent)){Text("Open a pack")};OutlinedButton(onClick={page="Cards"},modifier=Modifier.weight(1f)){Text("My cards",color=ink)}}
 Text(note,color=muted,fontSize=11.sp,modifier=Modifier.padding(top=12.dp))
 }
 "Cards"->Column(Modifier.fillMaxSize()){
 PageTitle("My cards",owned.size.toString()+" collected  •  "+team.size+"/5 equipped")
 Text("Tap cards to add or remove them from your team.",Modifier.padding(horizontal=20.dp),fontSize=11.sp,color=muted)
 LazyVerticalGrid(columns=GridCells.Fixed(2),contentPadding=PaddingValues(14.dp),horizontalArrangement=Arrangement.spacedBy(9.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){
 items(owned.map{cards[it]}){c->CollectionCard(c,c.id in team){if(c.id in team){team=team-c.id;note="Removed "+c.name+" from your team."}else if(team.size<5){team=team+c.id;note="Added "+c.name+" to your team."}else note="Your team is full. Remove one card first."}}
 }
 }
 "Battle"->Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=20.dp)){
 PageTitle("Battle club","Play solo or challenge someone beside you.")
 Text("MATCH TYPE",fontSize=10.sp,color=muted,fontWeight=FontWeight.Bold)
 Row(horizontalArrangement=Arrangement.spacedBy(8.dp),modifier=Modifier.padding(vertical=8.dp)){listOf("Practice bot","Pass & play").forEach{FilterChip(selected=mode==it,onClick={mode=it;hp1=100;hp2=100;turn=0;ended=false;note="New match ready."},label={Text(it,fontSize=11.sp)})}}
 Text(if(mode=="Pass & play")"Two players • one phone" else "Your team vs the training bot",fontSize=11.sp,color=muted)
 Spacer(Modifier.height(13.dp));BattleSide(if(mode=="Pass & play")"PLAYER 2" else "TRAINING BOT","🤡",hp2)
 Box(Modifier.fillMaxWidth().padding(10.dp),contentAlignment=Alignment.Center){Text("VS",fontSize=18.sp,fontWeight=FontWeight.Black,color=accent)}
 BattleSide("PLAYER 1",fighter.emoji,hp1)
 Text(note,Modifier.fillMaxWidth().padding(top=12.dp).clip(RoundedCornerShape(10.dp)).background(paper).padding(12.dp),fontSize=12.sp,color=ink)
 Spacer(Modifier.height(10.dp));Button(onClick={
 if(ended){hp1=100;hp2=100;turn=0;ended=false;note="New match. Player 1 goes first."}
 else if(turn%2==0){hp2=(hp2-fighter.attack).coerceAtLeast(0);note="Player 1 used "+fighter.name+"'s attack!";turn++}
 else{hp1=(hp1-24).coerceAtLeast(0);note=if(mode=="Pass & play")"Player 2 attacks!" else "The bot hits back!";turn++}
 if(hp1==0||hp2==0){ended=true;if(hp2==0){wins++;coins+=100;note="Player 1 wins! +100 coins."}else note="Player 2 wins this round."}
 },modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=accent)){Text(if(ended)"Play again" else if(turn%2==0)"Player 1 attack" else if(mode=="Pass & play")"Player 2 attack" else "Bot turn")}
 if(mode=="Pass & play")Text("Pass the phone after each turn.",color=muted,fontSize=10.sp,modifier=Modifier.padding(top=8.dp))
 Spacer(Modifier.height(18.dp));Text("ONLINE MATCHES",fontWeight=FontWeight.Bold,color=ink,fontSize=13.sp);Text("Online rooms need a shared game server. This version includes two-player pass-and-play; online matchmaking still needs backend setup.",color=muted,fontSize=11.sp)
 }
 "Packs"->Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)){
 PageTitle("Card packs","A surprise card in every pack.")
 Box(Modifier.fillMaxWidth().height(205.dp).clip(RoundedCornerShape(18.dp)).background(Color(0xFFE8D7C4)),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("✦",fontSize=28.sp,color=accent);Text("THE MIXED PACK",fontSize=22.sp,fontWeight=FontWeight.Black,color=ink);Text("ONE CARD • ANY RARITY",fontSize=9.sp,color=muted);Text("🃏",fontSize=45.sp)}}
 Text(owned.size.toString()+" / "+cards.size+" cards found",fontWeight=FontWeight.Bold,color=ink,modifier=Modifier.padding(top=16.dp));Text("No duplicate cards while there are still new ones to find.",fontSize=11.sp,color=muted)
 Button(onClick={val left=cards.filter{it.id !in owned};if(coins<150)note="You need 150 coins." else if(left.isEmpty())note="You found the whole collection!" else{val c=left.random();owned=owned+c.id;coins-=150;note="You found "+c.emoji+" "+c.name+" ("+c.rarity+")!"}},enabled=coins>=150&&owned.size<cards.size,modifier=Modifier.fillMaxWidth().padding(top=17.dp),colors=ButtonDefaults.buttonColors(containerColor=accent)){Text("Open pack • 150 coins")}
 Text(note,fontSize=11.sp,color=muted,modifier=Modifier.padding(top=12.dp))
 }
 else->Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)){
 PageTitle("The shop","Use coins to grow your collection.")
 ShopRow("🎁","Daily coins","100 free coins each day",if(daily)"CLAIMED" else "FREE"){if(!daily){coins+=100;daily=true;note="100 coins added!"}else note="Come back tomorrow."}
 ShopRow("🃏","Mixed pack","A new card for your collection","150 COINS"){val left=cards.filter{it.id !in owned};if(coins>=150&&left.isNotEmpty()){val c=left.random();coins-=150;owned=owned+c.id;note="You found "+c.emoji+" "+c.name+"!"}else note="Not enough coins or collection complete."}
 ShopRow("✨","Lucky pack","A better chance at rare cards","300 COINS"){if(coins>=300){coins-=300;val left=cards.filter{it.id !in owned};if(left.isNotEmpty()){val c=left.maxBy{listOf("COMMON","RARE","EPIC","LEGENDARY","MYTHIC").indexOf(it.rarity)+Random.nextInt(0,3)};owned=owned+c.id;note="Lucky pull: "+c.emoji+" "+c.name+"!"}else note="Collection complete."}else note="Not enough coins."}
 Text("Wallet: "+coins+" coins",fontWeight=FontWeight.Bold,color=accent,modifier=Modifier.padding(top=12.dp));Text(note,fontSize=11.sp,color=muted)
 }
 }
 }
 }}
}
@Composable private fun PageTitle(title:String,sub:String){Column(Modifier.padding(horizontal=20.dp,vertical=8.dp)){Text(title,fontSize=27.sp,fontWeight=FontWeight.Black,color=ink);Text(sub,fontSize=12.sp,color=muted)}}
@Composable private fun Metric(label:String,value:String,modifier:Modifier){Column(modifier.clip(RoundedCornerShape(12.dp)).background(paper).border(1.dp,edge,RoundedCornerShape(12.dp)).padding(11.dp)){Text(value,fontSize=20.sp,fontWeight=FontWeight.Bold,color=ink);Text(label,fontSize=9.sp,color=muted,letterSpacing=.7.sp)}}
@Composable private fun MiniCard(c:GameCard,modifier:Modifier){Column(modifier.clip(RoundedCornerShape(12.dp)).background(paper).border(1.dp,edge,RoundedCornerShape(12.dp)).padding(8.dp)){Text(c.emoji,fontSize=29.sp);Text(c.name,fontSize=10.sp,fontWeight=FontWeight.Bold,color=ink,maxLines=1);Text(c.rarity,fontSize=8.sp,color=tier(c.rarity))}}
@Composable private fun CollectionCard(c:GameCard,selected:Boolean,onClick:()->Unit){Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(paper).border(if(selected)2.dp else 1.dp,if(selected)accent else edge,RoundedCornerShape(14.dp)).clickable{onClick()}.padding(11.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(c.rarity,fontSize=8.sp,color=tier(c.rarity),fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));if(selected)Text("IN TEAM ✓",fontSize=8.sp,color=accent,fontWeight=FontWeight.Bold)};Box(Modifier.fillMaxWidth().height(78.dp),contentAlignment=Alignment.Center){Text(c.emoji,fontSize=49.sp)};Text(c.name,fontWeight=FontWeight.Bold,color=ink,fontSize=14.sp);Text(c.type,fontSize=9.sp,color=muted);Row(Modifier.padding(top=7.dp)){Text("♥ "+c.hp,fontSize=10.sp,color=ink,modifier=Modifier.weight(1f));Text("⚔ "+c.attack,fontSize=10.sp,color=accent,fontWeight=FontWeight.Bold)}}}
@Composable private fun BattleSide(name:String,emoji:String,hp:Int){Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp)).background(paper).border(1.dp,edge,RoundedCornerShape(15.dp)).padding(14.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(emoji,fontSize=38.sp);Spacer(Modifier.width(12.dp));Column{Text(name,fontWeight=FontWeight.Bold,color=ink,fontSize=12.sp);Text(hp.toString()+" / 100 HP",fontSize=10.sp,color=muted)}};Spacer(Modifier.height(8.dp));LinearProgressIndicator(progress={hp/100f},modifier=Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(8.dp)),color=if(hp<30)accent else Color(0xFF78845B),trackColor=edge)}}
@Composable private fun ShopRow(emoji:String,title:String,desc:String,price:String,onClick:()->Unit){Row(Modifier.fillMaxWidth().padding(vertical=6.dp).clip(RoundedCornerShape(13.dp)).background(paper).border(1.dp,edge,RoundedCornerShape(13.dp)).padding(12.dp),verticalAlignment=Alignment.CenterVertically){Text(emoji,fontSize=28.sp);Spacer(Modifier.width(10.dp));Column(Modifier.weight(1f)){Text(title,fontWeight=FontWeight.Bold,color=ink,fontSize=13.sp);Text(desc,color=muted,fontSize=10.sp)};TextButton(onClick=onClick){Text(price,color=accent,fontWeight=FontWeight.Bold,fontSize=10.sp)}}}
