<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  icon: String,
  alt: {
    type: String,
    default: 'icon'
  },
  text: String,
  to: String,
  bgColor: {
    type: String,
    default: 'bg-[#6F879C]'
  },
  textColor: {
    type: String,
    default: 'text-[#F2EDEC]'
  },
  borderColor: {
    type: String,
    default: 'border-[#6F879C]'
  }
})

const router = useRouter()

const handleClick = () => {
  if (props.to) router.push(props.to)
}
</script>

<template>
    <button @click="handleClick" :class="[
        'flex items-center p-[1vw] border-[0.15vw] w-fit rounded-[0.6vw] hover:scale-103 transition-scale ease-in-out duration-300',
        bgColor,
        borderColor,
    ]">
        <img :src="icon" :alt="alt" class="w-[1.5vw] h-[1.3vw] mr-[0.5vw]" />
        <p :class="[textColor, 'font-semibold text-[1.2vw]']">{{ text }}</p>
    </button>
</template>

<style scoped></style>