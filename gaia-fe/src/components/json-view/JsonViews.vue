<template>
  <div id="app">
    <div class="layout">
      <div class="setting">
        <ul>
          <li>
            <label>主题</label>
            <select v-model="theme">
              <option value="">默认</option>
              <option value="one-dark">one-dark</option>
              <option value="vs-code">vs-code</option>
            </select>
          </li>
          <li>
            <label>折叠全部</label>
            <select v-model="closed">
              <option :value="false">false</option>
              <option :value="true">true</option>
            </select>
          </li>

          <li>
            <label>deep</label>
            <select v-model="deep">
              <option v-for="item in 10" :value="item" :key="item">{{item}}</option>
            </select>
          </li>
          <li>
            <label>icon-style</label>
            <select v-model="iconStyle">
              <option value="square">square</option>
              <option value="circle">circle</option>
              <option value="triangle">triangle</option>
            </select>
          </li>
<!--          <li>-->
<!--            <label>icon-color</label>-->
<!--            <input type="color" v-model="color1">-->
<!--            <input type="color"  v-model="color2">-->
<!--          </li>-->
          <li>
            <label>font-size</label>
            <select v-model="fontSize">
              <option v-for="item in 10" :value="item+11" :key="item">{{item+11}}</option>
            </select>
          </li>
          <li>
            <label for="line-height">line-height</label>
            <select v-model="lineHeight">
              <option v-for="item in 20" :value="item*2+14" :key="item">{{item*2+14}}</option>
            </select>
          </li>
          <li>
            <button @click="copy(json)" style="border: 1px solid">{{copyBtn}}</button>
          </li>

        </ul>
      </div>
      <div class="content">
        <json-view :data="json" :theme="theme" :deep="deep" :iconStyle="iconStyle" :fontSize="fontSize" :lineHeight="lineHeight" :closed="closed" :iconColor="iconColor"/>
      </div>
    </div>
  </div>
</template>

<script>
import jsonView from '_c/json-view/index'

export default {
  name: 'json-views',
  props: {
    json: Object,
    deep: {
      type: Number,
      default: 10
    }
  },
  components: {
    jsonView
  },
  data () {
    return {
      theme: '',
      fontSize: 14,
      lineHeight: 24,
      closed: false,
      iconStyle: 'square',
      color1: '',
      color2: '',
      copyBtn: 'Copy'
    }
  },
  methods: {
    copy (text) {
      this.$copyText(JSON.stringify(text)).then(() => {
        this.$Message.success('拷贝成功')
        this.copyBtn = 'Copied ! ! !'
        setTimeout(() => {
          this.copyBtn = 'Copy'
        }, 3000)
      }).catch(e => this.$Message.error('拷贝失败: ' + e))
    }
  },
  computed: {
    iconColor () {
      const { color1, color2 } = this

      if (color1 && color2) {
        return [color1, color2]
      } else {
        return []
      }
    }
  }
}
</script>

<style scoped lang="less">
#app {
  .layout {
    //width: 1200px;
    width: 100%;
    margin: 0 auto;

    h1 {
      font-size: 26px;
      font-weight: 400;
      margin: 12px 0;
    }

    a {
      display: block;
      background-color: #f8f8f9;
      border-radius: 4px;
      padding: 8px 16px;
      border: 1px solid #edeff0;
      border-radius: 4px;
      margin-bottom: 20px;
      text-decoration: none;
      color: #2d8cf0;
    }

    .setting {
      border: 1px solid #ccc;
      padding: 10px 20px;

      h3 {
        line-height: 1.6;
        font-size: 18px;
        color: #333;
      }
    }

    ul {
      list-style: none;
      padding: 0;
      margin: 0;
      display: flex;
      align-items: center;

      li {
        display: flex;
        align-items: center;
        padding: 10px 0;
        padding-right: 20px;

        label {
          font-size: 13px;
          padding-right: 10px;
          color: #333;
        }

        select, button {
          display: block;
          width: 80px;
          height: 28px;
          background: #fff;
        }
      }
    }

    .content {
      border: 1px solid #ccc;
      border-top: none;
    }
  }
}
</style>
