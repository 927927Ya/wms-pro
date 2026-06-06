import { useState, useCallback, useRef } from 'react';

const usePageTable = (fetchFn, options = {}) => {
  const { defaultPageSize = 10 } = options;

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: defaultPageSize,
    total: 0,
  });
  const fetchLock = useRef(false);
  const lastParams = useRef({});

  const fetchData = useCallback(
    async (searchParams = {}, page = 1, size = pagination.pageSize) => {
      if (fetchLock.current) return;
      fetchLock.current = true;
      setLoading(true);
      try {
        lastParams.current = searchParams;
        const result = await fetchFn(searchParams);
        let list = [];
        let total = 0;
        if (Array.isArray(result)) {
          list = result;
          total = result.length;
        } else if (result?.records) {
          list = result.records;
          total = result.total || 0;
        }
        setData(list);
        setPagination({ current: page, pageSize: size, total });
      } catch {
        setData([]);
      } finally {
        setLoading(false);
        fetchLock.current = false;
      }
    },
    [fetchFn, pagination.pageSize]
  );

  const handleSearch = useCallback(
    (values) => {
      const clean = {};
      Object.keys(values || {}).forEach((k) => {
        if (values[k] !== undefined && values[k] !== null && values[k] !== '') {
          clean[k] = values[k];
        }
      });
      fetchData(clean, 1);
    },
    [fetchData]
  );

  const handlePageChange = useCallback(
    (page, size) => {
      fetchData(lastParams.current, page, size);
    },
    [fetchData]
  );

  const refresh = useCallback(() => {
    fetchData(lastParams.current, pagination.current, pagination.pageSize);
  }, [fetchData, pagination]);

  const initLoad = useCallback(() => {
    fetchData({}, 1);
  }, [fetchData]);

  return {
    data,
    loading,
    pagination,
    handleSearch,
    handlePageChange,
    refresh,
    initLoad,
  };
};

export default usePageTable;
