<template>
    <div id="chart">
      <apexchart ref="energyUsage" type=line height=350
      :options="chartOptions" :series="series" />
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { ENERGY_USAGES } from '../../store/modules/energy_history.methods';
import { ENERGY_USAGE_HISTORY_LENGTH_MILIS } from '../../constants';

export default {
  name: 'EnergyUsage',
  data() {
    return {
      chartOptions: {
        chart: {
          id: 'energy-usages',
        },
        animations: {
          enabled: true,
          easing: 'linear',
          dynamicAnimation: {
            speed: 5000,
          },
        },
        xaxis: {
          type: 'datetime',
          range: ENERGY_USAGE_HISTORY_LENGTH_MILIS,
        },
        yaxis: {
          decimalsInFloat: 3,
          title: {
            text: 'kWh',
          },
        },
        tooltip: {
          x: {
            format: 'HH:mm:ss',
          },
          y: {
            formatter: (value) => `${value.toFixed(4)}kWh`,
          },
        },
      },
      series: [{
        data: [],
      }],
    };
  },
  methods: {
    updateChart() {
      this.series[0].data = this[ENERGY_USAGES];
      if (this.$refs.energyUsage !== undefined) {
        this.$refs.energyUsage.updateSeries(this.series);
      }
    },
  },
  computed: {
    ...mapGetters([
      ENERGY_USAGES,
    ]),
  },
  watch: {
    [ENERGY_USAGES]() {
      this.updateChart();
    },
  },
  mounted() {
    this.updateChart();
  },
};
</script>

<style scoped>

</style>
