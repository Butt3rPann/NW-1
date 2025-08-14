<script setup>
const props = defineProps({
  phones: {
    type: Array,
    default: []
  },
  selectedIndex: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['update:selectedIndex']);

const updateSelected = (index) => {
  if (props.phones.length - 1 < index) {
    return
  }
  emit('update:selectedIndex', index);
}
</script>

<template>
  <div>
    <div class="flex mt-4 items-center">
      <button @click="updateSelected(selectedIndex === 0 ? phones.length - 1 : selectedIndex - 1)"
        class="text-[#6F879C] text-2xl md:text-3xl lg:text-4xl mr-3 rounded-full flex items-center justify-center transition duration-200 hover:bg-[#6F879C]/10">
      <
      </button>
      <div v-for="i in 4"
      class="bg-[#F0EDEC] w-13 h-13 md:w-17 md:h-17 lg:w-20 lg:h-20 xl:w-27 xl:h-27 m-1 xl:m-1.5 rounded-lg md:rounded-xl flex items-center justify-center overflow-hidden p-3"
      :class="{ 'border-[0.25vw] border-[#6F879C]': selectedIndex === (i - 1) }" @click="updateSelected(i - 1)">
        <p v-if="!phones || !phones[i-1]" class="text-[#332A1E] text-xs md:text-sm">No Picture</p>
        <img v-else :src="phones[i-1]" :alt="`Phone ${i-1}`" class="h-7 md:h-9 lg:h-11 xl:h-14" />
      </div>
      <button @click="updateSelected(selectedIndex === phones.length - 1 ? 0 : selectedIndex + 1)"
        class="text-[#6F879C] text-2xl md:text-3xl lg:text-4xl ml-3 rounded-full flex items-center justify-center transition duration-200 hover:bg-[#6F879C]/10">
      >
      </button>
    </div>
  </div>
</template>

<style scoped></style>
