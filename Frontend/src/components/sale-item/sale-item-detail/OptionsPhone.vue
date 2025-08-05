<script setup>
const props = defineProps({
  phones: {
    type: Array,
    required: true,
  },
  selectedIndex: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['update:selectedIndex']);

const updateSelected = (index) => {
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
      <div class="flex">
        <div v-for="(thumbnail, index) in phones" :key="index"
          class="bg-[#F0EDEC] w-13 h-13 md:w-17 md:h-17 lg:w-20 lg:h-20 xl:w-27 xl:h-27 m-1 xl:m-1.5 rounded-lg md:rounded-xl flex items-center justify-center overflow-hidden"
          :class="{ 'border-[0.25vw] border-[#6F879C]': selectedIndex === index }" @click="updateSelected(index)">
          <img :src="thumbnail" :alt="`Phone ${index}`" class="h-7 md:h-9 lg:h-11 xl:h-14" />
        </div>
      </div>
      <button @click="updateSelected(selectedIndex === phones.length - 1 ? 0 : selectedIndex + 1)"
        class="text-[#6F879C] text-2xl md:text-3xl lg:text-4xl ml-3 rounded-full flex items-center justify-center transition duration-200 hover:bg-[#6F879C]/10">
      >
      </button>
    </div>
  </div>
</template>

<style scoped></style>
