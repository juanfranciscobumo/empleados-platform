describe("Comments API Tests", () => {
  interface Comment {
    id: number;
    postId: number;
    name: string;
    email: string;
    body: string;
  }

  it("GET - Debería obtener todos los comentarios", () => {
    cy.apiGet<Comment[]>("/comments").then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.be.an("array");
      expect(response.body.length).to.be.greaterThan(0);
    });
  });

  it("GET - Debería filtrar comentarios por postId", () => {
    cy.apiGet<Comment[]>("/comments?postId=1").then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.be.an("array");
      response.body.forEach((comment) => {
        expect(comment.postId).to.eq(1);
      });
    });
  });
});
