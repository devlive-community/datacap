<template>
  <div>
    <Result type="error" title="404" style="margin-top: 30px;">
      <template #desc>
        Sorry, the page you visited does not exist. <br/>
        <br/>
        <Button type="primary" to="/" icon="md-home">
          Back Home
        </Button>
      </template>
    </Result>
  </div>
</template>
