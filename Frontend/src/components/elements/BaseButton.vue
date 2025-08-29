<script setup>
const props = defineProps({
  icon: String,
  text: String,
  disabled: Boolean,
  bgColor: {
    type: String,
    default: 'bg-[#FFFFFF]'
  },
  textColor: {
    type: String,
    default: 'text-[#6F879C]'
  },
  borderColor: {
    type: String,
    default: 'border-[#6F879C]'
  }
})
</script>

<template>
    <button
      :disabled="disabled"
      :class="[
        'flex items-center justify-center w-fit py-3 px-2 md:py-2 md:px-3 lg:py-3 lg:px-4 border-2 rounded-md',
              text === 'Save' || text === 'Submit' || text === 'Login' ? (disabled ? 'bg-[#6F879C]/50 border-transparent' : 'bg-[#4bbd80] border-transparent') : bgColor,
              borderColor,
              disabled ? 'cursor-not-allowed' : 'cursor-pointer'
          ]">
        <img v-if="icon" :src="icon" alt="Icon" class="w-4 md:w-5 mr-2" />
        <p :class="[textColor, 'font-medium md:font-semibold text-xs md:text-sm lg:text-base']">{{ text }}</p>
    </button>
</template>
 
<style scoped>

</style>
