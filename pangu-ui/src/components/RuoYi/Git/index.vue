<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'pangu-cloud-plus-Git',
  data() {
    return {
      url: 'https://gitee.com/dromara/pangu-cloud-plus'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
