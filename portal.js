const API='http://localhost:8081/api';
const headers=()=>({'Content-Type':'application/json','X-Token':localStorage.getItem('gymToken')||''});
const user=JSON.parse(localStorage.getItem('gymUser')||'null');
const pageRole=document.body.dataset.role;
if(!user||!localStorage.getItem('gymToken')) location.href='login.html';
else {
  const username=String(user.username||'').toLowerCase();
  const role=String(user.role||'');
  const actualRole=username==='admin'||role.includes('管理员')?'admin':username==='vip'||role.toUpperCase().includes('VIP')?'vip':'member';
  if(actualRole==='admin') location.href='index.html';
  else if(actualRole!==pageRole) location.href=actualRole==='vip'?'vip.html':'member.html';
  else {
    document.getElementById('realName').textContent=user.real_name||user.username;
    document.getElementById('userName').textContent=actualRole==='vip'?'VIP会员':'普通会员';
    const balance=document.getElementById('balance');
    if(balance) balance.textContent='账户余额 ¥'+Number(user.balance||0).toFixed(2);
    const cardType=document.getElementById('cardType');if(cardType&&user.card_type)cardType.textContent=user.card_type;
    const expireDate=document.getElementById('expireDate');if(expireDate&&user.expire_date)expireDate.textContent=String(user.expire_date).slice(0,10);
  }
}
async function loadCourses(){try{const r=await fetch(API+'/classes',{headers:headers()});const list=await r.json();document.getElementById('courseList').innerHTML=list.map(c=>`<div class="course-item"><i class="course-dot"></i><div><b>${c.name}</b><small>${c.coach||'教练待安排'} · ${c.time||'时间待定'} · ${c.current||0}/${c.capacity||20}人</small></div><button data-course="${c.id}">预约</button></div>`).join('');document.querySelectorAll('[data-course]').forEach(b=>b.addEventListener('click',()=>{b.textContent='已预约';b.disabled=true;alert('课程预约成功！')}));}catch(e){document.getElementById('courseList').innerHTML='<p>课程数据暂时无法加载，请确认后端已启动。</p>';}}
document.getElementById('logout').addEventListener('click',async()=>{await fetch(API+'/auth/logout',{method:'POST',headers:headers()}).catch(()=>{});localStorage.removeItem('gymToken');localStorage.removeItem('gymUser');location.href='login.html';});
document.getElementById('checkin').addEventListener('click',async()=>{if(!user||!user.member_id){alert('签到成功，祝你训练愉快！');return;}try{const r=await fetch(API+'/checkins',{method:'POST',headers:headers(),body:JSON.stringify({memberId:user.member_id})});if(!r.ok)throw new Error();alert('签到成功，管理员页面会同步更新。');}catch(e){alert('签到失败，请确认后端已启动。');}});
const upgrade=document.getElementById('upgradeVip');
if(upgrade) upgrade.addEventListener('click',async()=>{if(!user||!user.member_id){alert('演示账号不能升级，请使用注册账号登录。');return;}if(!confirm('确认使用账户余额升级 VIP 吗？升级后余额将归零。'))return;try{const r=await fetch(API+'/auth/upgrade',{method:'POST',headers:headers()});const data=await r.json();if(!r.ok)throw new Error(data.message||'升级失败');localStorage.setItem('gymUser',JSON.stringify(data.user));alert(data.message||'升级成功');location.href='vip.html';}catch(e){alert(e.message||'升级失败，请重试。');}});
loadCourses();
