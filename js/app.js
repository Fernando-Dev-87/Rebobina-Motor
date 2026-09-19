const AWG_TABLE = [
    {awg: 10, section: 5.261, diam: 2.588, res: 3.28},
    {awg: 11, section: 4.172, diam: 2.305, res: 4.13},
    {awg: 12, section: 3.309, diam: 2.053, res: 5.21},
    {awg: 13, section: 2.624, diam: 1.828, res: 6.57},
    {awg: 14, section: 2.081, diam: 1.628, res: 8.28},
    {awg: 15, section: 1.650, diam: 1.450, res: 10.45},
    {awg: 16, section: 1.309, diam: 1.291, res: 13.17},
    {awg: 17, section: 1.038, diam: 1.150, res: 16.61},
    {awg: 18, section: 0.823, diam: 1.024, res: 20.95},
    {awg: 19, section: 0.653, diam: 0.912, res: 26.41},
    {awg: 20, section: 0.518, diam: 0.812, res: 33.31},
    {awg: 21, section: 0.410, diam: 0.723, res: 42.00},
    {awg: 22, section: 0.326, diam: 0.644, res: 52.96},
    {awg: 23, section: 0.258, diam: 0.573, res: 66.79},
    {awg: 24, section: 0.205, diam: 0.511, res: 84.21},
    {awg: 25, section: 0.162, diam: 0.455, res: 106.2},
    {awg: 26, section: 0.129, diam: 0.405, res: 133.9},
    {awg: 27, section: 0.102, diam: 0.361, res: 168.9},
    {awg: 28, section: 0.0810, diam: 0.321, res: 212.9},
    {awg: 29, section: 0.0642, diam: 0.286, res: 268.5},
    {awg: 30, section: 0.0509, diam: 0.255, res: 338.6}
];

// Funções de Navegação
function switchTab(tab) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('nav button').forEach(el => el.classList.remove('active'));
    document.getElementById(`tab-${tab}`).classList.add('active');
    document.getElementById(`btn-${tab}`).classList.add('active');
    if(tab === 'oficina') renderAwgList();
}

// === LÓGICA TELA CÁLCULO (PLACA) ===
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

let lastResult = null;

function calculateRewind() {
    const cv = parseFloat(document.getElementById('cv-input').value) || 0;
    const rpm = parseInt(document.getElementById('rpm-input').value) || 1800;
    const volt = parseFloat(document.getElementById('volt-input').value) || 220;

    // Lógica Eletromagnética Simplificada (WEG Standard)
    const poles = rpm > 2000 ? 2 : (rpm > 1100 ? 4 : 6);
    const kw = cv * 0.7355;
    const amps = (kw * 1000) / (Math.sqrt(3) * volt * 0.86 * 0.82);

    // Escolha do fio (Elite: AWG 16+ para motores < 40CV)
    let wire = AWG_TABLE.find(w => w.section >= (amps / (poles * 4.5)));
    if(!wire) wire = AWG_TABLE[0];

    const turns = Math.round(poles * 12 * (220/volt));
    const weight = (cv * 0.42 * (4/poles)).toFixed(2);

    lastResult = { cv, amps, awg: wire.awg, section: wire.section, turns, weight };

    document.getElementById('res-wire').innerText = `AWG ${wire.awg}`;
    document.getElementById('res-section').innerText = `${wire.section.toFixed(3)} mm²`;
    document.getElementById('res-turns').innerText = turns;
    document.getElementById('res-cond').innerText = `${turns * 2} cond.`;
    document.getElementById('res-weight').innerText = `${weight} kg`;
    document.getElementById('res-amps').innerText = `${amps.toFixed(1)} A`;
    document.getElementById('res-bg').innerText = 36 / (poles * 3);
    document.getElementById('res-step').innerText = (36 / poles * 2/3).toFixed(1);

    document.getElementById('result-container').style.display = 'block';
    updateBudget();
    document.getElementById('result-container').scrollIntoView({ behavior: 'smooth' });
}

function updateBudget() {
    if (!lastResult) return;
    const price = parseFloat(document.getElementById('copper-price').value) || 0;
    const total = price * parseFloat(lastResult.weight);
    document.getElementById('res-cost').innerText = `R$ ${total.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
}

// === LÓGICA TELA FERRAGEM ===
function calculateFerragem() {
    const d = parseFloat(document.getElementById('f-diam').value) || 105;
    const l = parseFloat(document.getElementById('f-len').value) || 110;
    const poles = parseInt(document.getElementById('f-poles').value);

    // Área Polar
    const ap = (Math.PI * d / poles) * l / 100;
    const flux = 0.72 * 10000 * ap * (2/Math.PI) / 100000000;
    const powerWatts = ap * 120; // Estimativa empírica WEG simplificada
    const cv = powerWatts / 735.5;

    document.getElementById('f-res-cv').innerText = `${cv.toFixed(1)} CV`;
    document.getElementById('f-res-wire').innerText = `AWG 18`;
    document.getElementById('f-res-turns').innerText = Math.round(45 / cv + 10);
    document.getElementById('f-res-weight').innerText = (cv * 0.48).toFixed(2) + " kg";

    document.getElementById('f-result-container').style.display = 'block';
}

// === LÓGICA OFICINA (CAPACITORES E AWG) ===
let capVolt = 220;
function setCapVolt(v) {
    capVolt = v;
    document.getElementById('btn-cap110').classList.toggle('active', v === 110);
    document.getElementById('btn-cap220').classList.toggle('active', v === 220);
    calculateCapacitors();
}

function calculateCapacitors() {
    const cv = parseFloat(document.getElementById('cap-cv').value) || 1;
    let start = "", perm = "", minV = "";

    if (capVolt === 220) {
        start = `${Math.round(cv * 430)}-${Math.round(cv * 516)} µF`;
        perm = `${Math.round(cv * 25)}-${Math.round(cv * 35)} µF`;
        minV = "380V / 440V";
    } else {
        start = `${Math.round(cv * 250)}-${Math.round(cv * 300)} µF`;
        perm = `${Math.round(cv * 80)}-${Math.round(cv * 100)} µF`;
        minV = "250V";
    }

    document.getElementById('cap-start').innerText = start;
    document.getElementById('cap-perm').innerText = perm;
    document.getElementById('cap-min-v').innerText = minV;
}

function renderAwgList(filter = "") {
    const container = document.getElementById('awg-list');
    container.innerHTML = "";
    AWG_TABLE.filter(w => w.awg.toString().includes(filter)).forEach(w => {
        const card = document.createElement('div');
        card.className = 'awg-card';
        card.innerHTML = `<strong>AWG ${w.awg}</strong> <span>${w.section} mm²</span> <small>Ø ${w.diam} mm</small>`;
        container.appendChild(card);
    });
}

function filterAwg(val) {
    renderAwgList(val);
}

function shareToWhatsApp() {
    const text = `*Rebobina motor - Ficha Técnica*\nPotência: ${lastResult.cv} CV\nFio: AWG ${lastResult.awg}\nEspiras: ${lastResult.turns}\nPeso: ${lastResult.weight} kg\nGerado via Web App`;
    window.open(`https://wa.me/?text=${encodeURIComponent(text)}`, '_blank');
}

// Inicializar
calculateCapacitors();
renderAwgList();
