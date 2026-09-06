import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('errors');
const requestDuration = new Trend('req_duration');

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8081';

export const options = {
  stages: [
    { duration: '30s', target: 10 },
    { duration: '1m', target: 20 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    errors: ['rate<0.01'],
  },
};

export default function () {
  // GET /employees
  const getRes = http.get(`${BASE_URL}/employees`);
  requestDuration.add(getRes.timings.duration);
  
  check(getRes, {
    'GET /employees status 200': (r) => r.status === 200,
    'GET /employees has data': (r) => JSON.parse(r.body).length > 0,
  }) || errorRate.add(1);

  // POST /employees
  const payload = JSON.stringify({
    name: `Test Employee ${Date.now()}`,
    role: 'Tester',
  });

  const postRes = http.post(`${BASE_URL}/employees`, payload, {
    headers: { 'Content-Type': 'application/json' },
  });
  requestDuration.add(postRes.timings.duration);

  check(postRes, {
    'POST /employees status 200': (r) => r.status === 200,
    'POST /employees has id': (r) => JSON.parse(r.body).id !== undefined,
  }) || errorRate.add(1);

  // GET /employees/{id}
  const employeeId = JSON.parse(postRes.body).id;
  const getOneRes = http.get(`${BASE_URL}/employees/${employeeId}`);
  requestDuration.add(getOneRes.timings.duration);

  check(getOneRes, {
    'GET /employees/{id} status 200': (r) => r.status === 200,
    'GET /employees/{id} correct name': (r) => JSON.parse(r.body).name.startsWith('Test Employee'),
  }) || errorRate.add(1);

  // PUT /employees/{id}
  const updatePayload = JSON.stringify({
    name: `Updated Employee ${Date.now()}`,
    role: 'Senior Tester',
  });

  const putRes = http.put(`${BASE_URL}/employees/${employeeId}`, updatePayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  requestDuration.add(putRes.timings.duration);

  check(putRes, {
    'PUT /employees/{id} status 200': (r) => r.status === 200,
  }) || errorRate.add(1);

  // DELETE /employees/{id}
  const deleteRes = http.del(`${BASE_URL}/employees/${employeeId}`);
  requestDuration.add(deleteRes.timings.duration);

  check(deleteRes, {
    'DELETE /employees/{id} status 200': (r) => r.status === 200,
  }) || errorRate.add(1);

  sleep(1);
}
