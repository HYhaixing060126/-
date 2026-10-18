document.getElementById('loginForm').addEventListener('submit',async function(e){e.preventDefault();const f=new FormData(e.target);const msg=document.getElementById('message');msg.textContent='正在登录...';try{const r=await fetch('http://localhost:8081/api/auth/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({username:f.get('username'),password:f.get('password')})});const d=await r.json();if(!r.ok)throw new Error(d.message||'登录失败');localStorage.setItem('gymToken',d.token);localStorage.setItem('gymUser',JSON.stringify(d.user));
const username=String(d.user.username||'').toLowerCase();const role=String(d.user.role||'');
// 使用账号作为稳定的角色依据，避免数据库中的中文角色字段乱码导致所有人进入同一页面。
location.href=username==='admin'||role.includes('管理员')?'index.html':username==='vip'||role.toUpperCase().includes('VIP')?'vip.html':'member.html';
}catch(err){msg.textContent=err.message+'（请先启动 MySQL 和 Spring Boot 后端）'}});
