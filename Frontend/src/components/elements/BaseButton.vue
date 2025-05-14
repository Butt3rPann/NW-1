<script setup>
defineProps({
  icon: String,
  text: String,
  to: String,
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
    <button :class="[
        'flex items-center py-3 px-4 border-2 w-fit rounded-md hover:scale-103 transition-scale ease-in-out duration-300',
        bgColor,
        borderColor,
    ]">
        <img v-if="icon" :src="icon" alt="Icon" class="w-5.5 mr-2" />
        <p :class="[textColor, 'font-semibold text-base']">{{ text }}</p>
    </button>
</template>
 
<style scoped>

</style>