<template>
    <b-container fluid>
      <b-col align-self="center">
        <apexchart
          type=radialBar
          width=420
          :options="chartOptions"
          :series="series"
          ref="energyStorage"
        />
      </b-col>
    </b-container>
</template>

<script>
import { mapGetters } from 'vuex';
import { ENERGY_STORAGE } from '../../store/modules/energy_history.methods';

export default {
  name: 'EnergyStorage',
  data() {
    return {
      series: [],
      chartOptions: {
        plotOptions: {
          radialBar: {
            offsetY: -10,
            startAngle: 0,
            endAngle: 270,
            hollow: {
              margin: 5,
              size: '30%',
              background: 'transparent',
              image: undefined,
            },
            dataLabels: {
              name: {
                show: false,
              },
              value: {
                show: false,
              },
              total: {
                show: true,
                label: 'Total',
                formatter: () => this.formattedTotalStorage,
              },
            },
          },
        },
        legend: {
          show: true,
          floating: true,
          fontSize: '14px',
          position: 'left',
          offsetX: -30,
          offsetY: 20,
          markers: {
            size: 0,
          },
          formatter: (seriesName, opts) => seriesName + ":  " + this.formattedStorage[opts.seriesIndex],
          itemMargin: {
            horizontal: 1,
          }
        },
        responsive: [{
          breakpoint: 480,
          options: {
            legend: {
              show: false
            }
          }
        }],
        labels: [],
      },
    };
  },
  methods: {
    updateChart() {
        this.series = this.relativeStorage;
        if (this.$refs.energyStorage !== undefined) {
            this.$refs.energyStorage.updateSeries(this.series);
            this.$refs.energyStorage.updateOptions({
              labels: this.labels,
            });
        }
    },
  },
  computed: {
    ...mapGetters([
      ENERGY_STORAGE,
    ]),
    relativeStorage() {
      return this[ENERGY_STORAGE].map(entry => entry.currentPowerStorage / entry.totalPowerStorage * 100);
    },
    formattedStorage() {
      return this[ENERGY_STORAGE].map(entry => `${entry.currentPowerStorage.toFixed(1)}/${entry.totalPowerStorage.toFixed(0)}`);
    },
    formattedTotalStorage() {
      const { current, total } = this[ENERGY_STORAGE].reduce(
        ({current, total}, entry) => ({ current: current + entry.currentPowerStorage, total: total + entry.totalPowerStorage }),
        { current: 0, total: 0 });
      return `${current.toFixed(0)}/${total.toFixed(0)}`;
    },
    labels() {
      return this[ENERGY_STORAGE].map(entry => entry.name);
    }
  },
  watch: {
    [ENERGY_STORAGE]() {
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
