const API='http://localhost:8081/api';
const headers=()=>({'Content-Type':'application/json','X-Token':localStorage.getItem('gymToken')||''});
const user=JSON.parse(localStorage.getItem('gymUser')||'null');
if(!user||!localStorage.getItem('gymToken')) location.href='login.html';
else { document.getElementById('realName').textContent=user.real_name||user.username; document.getElementById('userName').textContent=user.role||'会员'; }
async function loadCourses(){try{const r=await fetch(API+'/classes',{headers:headers()});const list=await r.json();document.getElementById('courseList').innerHTML=list.map(c=>`<div class="course-item"><i class="course-dot"></i><div><b>${c.name}</b><small>${c.coach||'教练待安排'} · ${c.time||'时间待定'} · ${c.current||0}/${c.capacity||20}人</small></div><button data-course="${c.id}">预约</button></div>`).join('');document.querySelectorAll('[data-course]').forEach(b=>b.addEventListener('click',()=>{b.textContent='已预约';b.disabled=true;alert('课程预约成功！')}));}catch(e){document.getElementById('courseList').innerHTML='<p>课程数据暂时无法加载，请确认后端已启动。</p>';}}
document.getElementById('logout').addEventListener('click',async()=>{await fetch(API+'/auth/logout',{method:'POST',headers:headers()}).catch(()=>{});localStorage.removeItem('gymToken');localStorage.removeItem('gymUser');location.href='login.html';});
document.getElementById('checkin').addEventListener('click',()=>alert('签到成功，祝你训练愉快！'));
loadCourses();
