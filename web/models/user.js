module.exports = function(sequelize, DataTypes) {
  var User = sequelize.define("User", {
    id: { 
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    rid: {
      type: DataTypes.UUID,
      references: 'Rooms',
      referencesKey: 'id',
      allowNull: true
    },
    fbid: {
      type: DataTypes.STRING,
      unique: true,
      allowNull: false
    },
    fbToken: {
      type: DataTypes.STRING,
      allowNull: false
    },
    name: DataTypes.STRING,
    pic: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        User.belongsTo(models.Room, { foreignKey: 'rid' });
        User.hasMany(models.Card, { foreignKey: 'uid' });
        User.hasMany(models.Vote, { foreignKey: 'uid' });
      }
    }
  });

  return User;
};