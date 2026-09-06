describe("Posts API Tests", () => {
  interface Post {
    id: number;
    title: string;
    body: string;
    userId: number;
  }

  interface PostInput {
    title: string;
    body: string;
    userId: number;
  }

  it("GET - Debería obtener todos los posts", () => {
    cy.apiGet<Post[]>("/posts").then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.be.an("array");
      expect(response.body.length).to.be.greaterThan(0);
    });
  });

  it("GET - Debería obtener un post por ID", () => {
    cy.apiGet<Post>("/posts/1").then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property("id", 1);
      expect(response.body).to.have.property("title");
      expect(response.body).to.have.property("body");
      expect(response.body).to.have.property("userId");
    });
  });

  it("POST - Debería crear un nuevo post", () => {
    const newPost: PostInput = {
      title: "Mi Post de Prueba",
      body: "Contenido del post de prueba",
      userId: 1,
    };

    cy.apiPost<Post>("/posts", newPost).then((response) => {
      expect(response.status).to.eq(201);
      expect(response.body).to.have.property("id");
      expect(response.body.title).to.eq(newPost.title);
      expect(response.body.body).to.eq(newPost.body);
      expect(response.body.userId).to.eq(newPost.userId);
    });
  });

  it("PUT - Debería actualizar un post existente", () => {
    const updatedPost: PostInput = {
      title: "Post Actualizado",
      body: "Contenido actualizado",
      userId: 1,
    };

    cy.apiPut<Post>("/posts/1", updatedPost).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body.title).to.eq(updatedPost.title);
      expect(response.body.body).to.eq(updatedPost.body);
    });
  });

  it("DELETE - Debería eliminar un post", () => {
    cy.apiDelete("/posts/1").then((response) => {
      expect(response.status).to.eq(200);
    });
  });

  it("GET - Debería filtrar posts por userId", () => {
    cy.apiGet<Post[]>("/posts?userId=1").then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.be.an("array");
      response.body.forEach((post) => {
        expect(post.userId).to.eq(1);
      });
    });
  });
});
