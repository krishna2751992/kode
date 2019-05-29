export const data = [
  {
    col_id: '0',
    newsId: '1',
    status: 'Active',
    postedOn: '01/21/2019',
    newsEvent:
      'Google News is a news aggregator and app developed by Google. It presents a continuous, customizable flow of articles officially in January 2006'
  },
  {
    col_id: '1',
    newsId: '3',
    status: 'Archive',
    postedOn: '01/30/2019',
    newsEvent:
      'React is a JavaScript library for building user interfaces. It is maintained by Facebook and a community of individual developers and companies.'
  },
  {
    col_id: '2',
    newsId: '4',
    status: 'Active',
    postedOn: '02/21/2019',
    newsEvent:
      'Lodash is a JavaScript library which provides utility functions for common programming tasks using the functional programming paradigm. Wikipedia'
  },
  {
    col_id: '3',
    newsId: '2',
    status: 'Archive',
    postedOn: '01/29/2019',
    newsEvent: '002, and released officially in January 2006. Wikipedia'
  }
];
export function getData() {
  return data;
}
