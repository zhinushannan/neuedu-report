let sortUtil = {
  sort: function (obj) {
    return function (src, tar) {
      //获取比较的值
      let v1 = src[obj];
      let v2 = tar[obj];
      if (v1 === 12 && v2 === 1) {
        return -1;
      }
      if (v1 > v2) {
        return 1;
      }
      if (v1 < v2) {
        return -1;
      }
      return 0;
    };
  }
}


exports.sort = sortUtil.sort

