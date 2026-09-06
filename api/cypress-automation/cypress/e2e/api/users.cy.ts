describe("Users API Tests", () => {
  interface User {
    id: number;
    name: string;
    username: string;
    email: string;
    phone: string;
  }

  it("GET - Debería obtener todos los usuarios", () => {
    cy.apiGet<User[]>("/users").then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.be.an("array");
      expect(response.body.length).to.eq(10);
    });
  });

  it("GET - Debería obtener un usuario por ID", () => {
    cy.apiGet<User>("/users/1").then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property("id", 1);
      expect(response.body).to.have.property("name");
      expect(response.body).to.have.property("email");
    });
  });

  it("GET - Debería obtener posts de un usuario específico", () => {
    cy.request({
      method: "GET",
      url: "/users/1/posts",
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.be.an("array");
    });
  });
});
