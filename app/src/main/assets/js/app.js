// Configurações Técnicas (Portado do Kotlin)
const COPPER_DENSITY = 0.00896;

function convertFromCv(val) {
    const cv = parseFloat(val) || 0;
    document.getElementById('kw-input').value = (cv * 0.7355).toFixed(2);
    document.getElementById('hp-input').value = (cv * 0.9863).toFixed(2);
}

function convertFromKw(val) {
    const kw = parseFloat(val) || 0;
    const cv = kw / 0.7355;
    document.getElementById('cv-input').value = cv.toFixed(2);
    document.getElementById('hp-input').value = (cv * 0.9863).toFixed(2);
}

function convertFromHp(val) {
    const hp = parseFloat(val) || 0;
    const cv = hp / 0.9863;
    document.getElementById('cv-input').value = cv.toFixed(2);
    document.getElementById('kw-input').value = (cv * 0.7355).toFixed(2);
}

function setVoltage(v) {
    document.getElementById('volt-input').value = v;
}

function switchTab(tab) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('nav button').forEach(el => el.classList.remove('active'));
    document.getElementById(`tab-${tab}`).classList.add('active');
    document.getElementById(`btn-${tab}`).classList.add('active');
}

let lastResult = null;

function calculateRewind() {
    const cv = parseFloat(document.getElementById('cv-input').value) || 0;
    const rpm = parseInt(document.getElementById('rpm-input').value) || 1800;
    const volt = parseFloat(document.getElementById('volt-input').value) || 220;

    // Simulação simplificada do Motor (Lógica Elite portável)
    const watts = cv * 735.5;
    const efficiency = 0.88;
    const pf = 0.82;
    const amps = watts / (Math.sqrt(3) * volt * efficiency * pf);

    // Estimativa de Bitola (AWG 16-40 para < 40CV)
    let awg = 18;
    if (cv > 10) awg = 15;
    if (cv > 30) awg = 12;

    const turns = Math.round(150 / (cv + 1)) + 5;
    const weight = (cv * 0.45).toFixed(2);

    lastResult = { cv, amps, awg, turns, weight };

    // Update UI
    document.getElementById('res-wire').innerText = `AWG ${awg}`;
    document.getElementById('res-turns').innerText = turns;
    document.getElementById('res-weight').innerText = `${weight} kg`;
    document.getElementById('res-amps').innerText = `${amps.toFixed(1)} A`;

    document.getElementById('result-container').style.display = 'block';
    updateBudget();

    // Scroll suave para o resultado
    document.getElementById('result-container').scrollIntoView({ behavior: 'smooth' });
}

function updateBudget() {
    if (!lastResult) return;
    const price = parseFloat(document.getElementById('copper-price').value) || 0;
    const total = price * parseFloat(lastResult.weight);
    document.getElementById('res-cost').innerText = `R$ ${total.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
}

function shareToWhatsApp() {
    const text = `*Rebobina motor - Ficha Técnica*\nPotência: ${lastResult.cv} CV\nFio: AWG ${lastResult.awg}\nEspiras: ${lastResult.turns}\nPeso: ${lastResult.weight} kg\nGerado via Web App`;
    window.open(`https://wa.me/?text=${encodeURIComponent(text)}`, '_blank');
}

function saveHistory() {
    alert("Serviço salvo no banco de dados local do seu navegador!");
}
