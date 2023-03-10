function transparentBackgroundChartExtender() {
    this.cfg.seriesDefaults.shadow = false;
    this.cfg.grid = {shadow: false, gridLineColor: '#c0c5c7', borderColor: '#c0c5c7', background: 'transparent', borderWidth: '1'}
}

function transparentBackgroundChartExtenderForPanel() {
    this.cfg.seriesDefaults.shadow = false;
    this.cfg.grid = {shadow: false, gridLineColor: '#c0c5c7', background: 'transparent', borderWidth: '0'}
}

function mgExtender() {
    this.cfg.seriesDefaults.rendererOptions.background = '#fbfcfd';
    this.cfg.seriesDefaults.rendererOptions.ringWidth = '5';
    this.cfg.seriesDefaults.rendererOptions.ringColor = '#c0c5c7';
}
